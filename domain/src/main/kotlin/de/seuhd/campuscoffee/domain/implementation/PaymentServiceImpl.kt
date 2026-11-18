package de.seuhd.campuscoffee.domain.implementation

import de.seuhd.campuscoffee.domain.exceptions.ValidationException
import de.seuhd.campuscoffee.domain.model.Payment
import de.seuhd.campuscoffee.domain.ports.api.PaymentService
import de.seuhd.campuscoffee.domain.ports.data.PaymentDataService
import de.seuhd.campuscoffee.domain.ports.data.UserDataService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/** Domain implementation of [PaymentService]. */
@Service
class PaymentServiceImpl(
    private val paymentDataService: PaymentDataService,
    private val userDataService: UserDataService
) : PaymentService {
    @Transactional
    override fun create(payment: Payment): Payment {
        if (payment.amount < MIN_AMOUNT || payment.amount > MAX_AMOUNT) {
            throw ValidationException("A payment amount must be between €0.01 and €1,000.")
        }
        if (payment.note?.length ?: 0 > MAX_NOTE_LENGTH) {
            throw ValidationException("A payment note must have at most 500 characters.")
        }
        val user = userDataService.getByIdForUpdate(requireNotNull(payment.user.id))
        val stored = paymentDataService.upsert(payment.copy(id = null, createdAt = null, updatedAt = null, user = user))
        userDataService.upsert(user.copy(balance = user.balance + payment.amount))
        return stored
    }

    private companion object {
        val MIN_AMOUNT: BigDecimal = BigDecimal("0.01")
        val MAX_AMOUNT: BigDecimal = BigDecimal("1000.00")
        const val MAX_NOTE_LENGTH = 500
    }
}
