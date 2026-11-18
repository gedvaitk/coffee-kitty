package de.seuhd.campuscoffee.domain.ports.api

import de.seuhd.campuscoffee.domain.model.Expense

/** API port for recording expenses. */
interface ExpenseService {
    /**
     * Records [expense] and increases its user's balance.
     *
     * @param expense the expense to record
     */
    fun create(expense: Expense): Expense

    /** Returns all recorded expenses. */
    fun getAll(): List<Expense>
}
