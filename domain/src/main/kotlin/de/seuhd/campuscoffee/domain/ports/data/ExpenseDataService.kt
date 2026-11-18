package de.seuhd.campuscoffee.domain.ports.data

import de.seuhd.campuscoffee.domain.model.Expense
import java.util.UUID

/** Data port for recorded expenses. */
interface ExpenseDataService : CrudDataService<Expense, UUID>
