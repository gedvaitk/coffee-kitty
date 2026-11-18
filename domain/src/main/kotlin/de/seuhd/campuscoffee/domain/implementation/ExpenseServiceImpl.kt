package de.seuhd.campuscoffee.domain.implementation

import de.seuhd.campuscoffee.domain.exceptions.ValidationException
import de.seuhd.campuscoffee.domain.model.Expense
import de.seuhd.campuscoffee.domain.model.ExpenseKind
import de.seuhd.campuscoffee.domain.ports.api.ExpenseService
import de.seuhd.campuscoffee.domain.ports.data.ExpenseDataService
import de.seuhd.campuscoffee.domain.ports.data.UserDataService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/** Domain implementation of [ExpenseService]. */
@Service
class ExpenseServiceImpl(
    private val expenseDataService: ExpenseDataService,
    private val userDataService: UserDataService
) : ExpenseService {
    @Transactional
    override fun create(expense: Expense): Expense {
        validate(expense)
        val user = userDataService.getByIdForUpdate(requireNotNull(expense.user.id))
        val stored = expenseDataService.upsert(expense.copy(id = null, createdAt = null, updatedAt = null, user = user))
        userDataService.upsert(user.copy(balance = user.balance + expense.amount))
        return stored
    }

    override fun getAll(): List<Expense> = expenseDataService.getAll()

    /** Validates the amount, note, and fields required by the expense kind. */
    private fun validate(expense: Expense) {
        validateAmountAndNote(expense)
        validateKind(expense)
    }

    /** Validates the amount and optional note. */
    private fun validateAmountAndNote(expense: Expense) {
        if (expense.amount < MIN_AMOUNT || expense.amount > MAX_AMOUNT) {
            throw ValidationException("An expense amount must be between €0.01 and €1,000.")
        }
        if (expense.note?.length ?: 0 > MAX_NOTE_LENGTH) {
            throw ValidationException("An expense note must have at most 500 characters.")
        }
    }

    /** Validates the fields required by the expense kind. */
    private fun validateKind(expense: Expense) {
        when (expense.kind) {
            ExpenseKind.COFFEE -> {
                if (expense.beanName.isNullOrBlank() || expense.weightGrams == null || expense.weightGrams <= 0) {
                    throw ValidationException("A coffee expense needs a bean name and a positive weight in grams.")
                }
            }

            ExpenseKind.FILTER,
            ExpenseKind.MILK,
            ExpenseKind.SUGAR,
            ExpenseKind.OTHER -> {
                if (expense.itemName.isNullOrBlank()) {
                    throw ValidationException("This expense kind needs an item name.")
                }
            }
        }
    }

    private companion object {
        val MIN_AMOUNT: BigDecimal = BigDecimal("0.01")
        val MAX_AMOUNT: BigDecimal = BigDecimal("1000.00")
        const val MAX_NOTE_LENGTH = 500
    }
}
