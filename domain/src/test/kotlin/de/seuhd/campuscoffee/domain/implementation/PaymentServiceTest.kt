package de.seuhd.campuscoffee.domain.implementation

import de.seuhd.campuscoffee.domain.exceptions.ValidationException
import de.seuhd.campuscoffee.domain.model.Payment
import de.seuhd.campuscoffee.domain.model.User
import de.seuhd.campuscoffee.domain.ports.data.PaymentDataService
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

/** Unit tests for [PaymentServiceImpl]. */
class PaymentServiceTest {
    private val paymentDataService: PaymentDataService = mock()
    private val userDataService: UserDataService = mock()
    private val service = PaymentServiceImpl(paymentDataService, userDataService)

    private val user =
        User(
            id = UUID(0L, 1L),
            loginName = "jane",
            emailAddress = "jane@example.com",
            firstName = "Jane",
            lastName = "Doe",
            balance = BigDecimal("0.00")
        )

    @Test
    fun `create payment increases the user's balance by the amount`() {
        val payment = Payment(user = user, amount = BigDecimal("50.00"), note = "Cash")
        whenever(userDataService.getByIdForUpdate(requireNotNull(user.id))).thenReturn(user)
        whenever(paymentDataService.upsert(any())).thenAnswer { it.arguments[0] as Payment }

        val result = service.create(payment)

        assertThat(result).isEqualTo(payment)
        verify(userDataService).upsert(user.copy(balance = BigDecimal("50.00")))
    }

    @Test
    fun `create payment above the maximum throws ValidationException and writes nothing`() {
        val payment = Payment(user = user, amount = BigDecimal("1000.01"))

        assertThrows<ValidationException> { service.create(payment) }

        verify(paymentDataService, never()).upsert(any())
        verify(userDataService, never()).getByIdForUpdate(any())
    }
}
