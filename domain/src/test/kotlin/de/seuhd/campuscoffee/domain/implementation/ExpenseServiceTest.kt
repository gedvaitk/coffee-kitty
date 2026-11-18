package de.seuhd.campuscoffee.domain.implementation

import de.seuhd.campuscoffee.domain.exceptions.ValidationException
import de.seuhd.campuscoffee.domain.model.Expense
import de.seuhd.campuscoffee.domain.model.ExpenseKind
import de.seuhd.campuscoffee.domain.model.User
import de.seuhd.campuscoffee.domain.ports.data.ExpenseDataService
import de.seuhd.campuscoffee.domain.ports.data.UserDataService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.UUID

/** Unit tests for [ExpenseServiceImpl]. */
class ExpenseServiceTest {
    private val expenseDataService: ExpenseDataService = mock()
    private val userDataService: UserDataService = mock()
    private val service = ExpenseServiceImpl(expenseDataService, userDataService)

    private val user =
        User(
            id = UUID(0L, 1L),
            loginName = "jane",
            emailAddress = "jane@example.com",
            firstName = "Jane",
            lastName = "Doe",
            balance = BigDecimal("2.00")
        )

    @Test
    fun `create coffee expense increases the user's balance by the amount`() {
        val expense =
            Expense(
                user = user,
                amount = BigDecimal("15.55"),
                kind = ExpenseKind.COFFEE,
                beanName = "Espresso",
                weightGrams = 1000
            )
        whenever(userDataService.getByIdForUpdate(requireNotNull(user.id))).thenReturn(user)
        whenever(expenseDataService.upsert(any())).thenAnswer { it.arguments[0] as Expense }

        val result = service.create(expense)

        assertThat(result).isEqualTo(expense)
        verify(userDataService).upsert(user.copy(balance = BigDecimal("17.55")))
    }

    @Test
    fun `create filter expense increases the user's balance by the amount`() {
        val expense =
            Expense(
                user = user,
                amount = BigDecimal("10.00"),
                kind = ExpenseKind.FILTER,
                itemName = "Paper filters",
                note = "Monthly supply"
            )
        whenever(userDataService.getByIdForUpdate(requireNotNull(user.id))).thenReturn(user)
        whenever(expenseDataService.upsert(any())).thenAnswer { it.arguments[0] as Expense }

        service.create(expense)

        verify(userDataService).upsert(user.copy(balance = BigDecimal("12.00")))
    }

    @Test
    fun `create coffee expense with zero weight throws ValidationException and writes nothing`() {
        val expense =
            Expense(
                user = user,
                amount = BigDecimal("15.55"),
                kind = ExpenseKind.COFFEE,
                beanName = "Espresso",
                weightGrams = 0
            )

        assertThrows<ValidationException> { service.create(expense) }

        verify(expenseDataService, never()).upsert(any())
        verify(userDataService, never()).getByIdForUpdate(any())
    }
}
