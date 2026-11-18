package de.seuhd.campuscoffee.api.dtos

import java.math.BigDecimal

/**
 * Response body for [de.seuhd.campuscoffee.api.controller.BalanceController], with the login name and balance of a
 * user.
 *
 * @property loginName the login name of the user
 * @property balance the user's balance in euros
 */
data class BalanceDto(
    val loginName: String,
    val balance: BigDecimal
)
