package de.seuhd.campuscoffee.domain.implementation

import de.seuhd.campuscoffee.domain.model.CoffeeConsumption
import de.seuhd.campuscoffee.domain.model.User
import de.seuhd.campuscoffee.domain.ports.data.CoffeeConsumptionDataService
import de.seuhd.campuscoffee.domain.ports.data.UserDataService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.UUID

/** Unit tests for the balance change when a user adds a coffee. */
class CoffeeBalanceServiceTest {
    private val consumptionDataService: CoffeeConsumptionDataService = mock()
    private val userDataService: UserDataService = mock()
    private val service = CoffeeConsumptionServiceImpl(consumptionDataService, userDataService)

    @Test
    fun `applyDelta charges 50 cents and allows the balance to become negative`() {
        val user =
            User(
                id = UUID(0L, 1L),
                loginName = "jane",
                emailAddress = "jane@example.com",
                firstName = "Jane",
                lastName = "Doe",
                balance = BigDecimal("0.25")
            )
        val consumption = CoffeeConsumption(user = user, count = 0)
        whenever(consumptionDataService.getByUserIdForUpdate(requireNotNull(user.id))).thenReturn(consumption)
        whenever(userDataService.getByIdForUpdate(requireNotNull(user.id))).thenReturn(user)
        whenever(consumptionDataService.upsert(any())).thenAnswer { it.arguments[0] as CoffeeConsumption }
        whenever(userDataService.upsert(any())).thenAnswer { it.arguments[0] as User }

        val result = service.applyDelta(requireNotNull(user.id), 1)

        assertThat(result.count).isEqualTo(1)
        assertThat(result.user.balance).isEqualByComparingTo("-0.25")
        verify(userDataService).upsert(user.copy(balance = BigDecimal("-0.25")))
    }
}
