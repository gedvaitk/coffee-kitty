package de.seuhd.campuscoffee.domain.ports.api

import de.seuhd.campuscoffee.domain.model.Payment

/** API port for recording payments. */
interface PaymentService {
    /**
     * Records [payment] and increases its user's balance.
     *
     * @param payment the payment to record
     */
    fun create(payment: Payment): Payment
}
