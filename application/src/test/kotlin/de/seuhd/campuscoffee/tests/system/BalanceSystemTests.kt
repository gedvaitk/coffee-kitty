package de.seuhd.campuscoffee.tests.system

import de.seuhd.campuscoffee.api.dtos.BalanceDto
import de.seuhd.campuscoffee.api.dtos.PaymentDto
import de.seuhd.campuscoffee.domain.model.persistedId
import de.seuhd.campuscoffee.tests.SystemTestUtils.client
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.returnResult
import java.math.BigDecimal
import java.util.UUID

/** System tests for reading a user's balance through `/api/users/{userId}/balance`. */
class BalanceSystemTests : AbstractSystemTest() {
    private val loginName = "maxmustermann"

    private fun fixtureUserId(): UUID = fixtureUser(loginName).persistedId

    @Test
    fun `getting the balance of a fixture user returns 200 OK with a zero balance`() {
        val result =
            client()
                .get()
                .uri("/api/users/{userId}/balance", fixtureUserId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult<BalanceDto>()

        assertThat(result.status.value()).isEqualTo(200)
        assertThat(result.responseBody!!.loginName).isEqualTo(loginName)
        assertThat(result.responseBody!!.balance).isEqualByComparingTo(BigDecimal("0.00"))
    }

    @Test
    fun `getting the balance after a payment returns the raised balance`() {
        val userId = fixtureUserId()
        client()
            .post()
            .uri("/api/users/{userId}/payments", userId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(PaymentDto(amount = BigDecimal("50.00"), note = "Cash"))
            .exchange()

        val result =
            client()
                .get()
                .uri("/api/users/{userId}/balance", userId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult<BalanceDto>()

        assertThat(result.status.value()).isEqualTo(200)
        assertThat(result.responseBody!!.balance).isEqualByComparingTo(BigDecimal("50.00"))
    }
}
