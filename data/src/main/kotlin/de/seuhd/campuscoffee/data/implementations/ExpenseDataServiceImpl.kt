package de.seuhd.campuscoffee.data.implementations

import de.seuhd.campuscoffee.data.mapper.ExpenseEntityMapper
import de.seuhd.campuscoffee.data.persistence.entities.ExpenseEntity
import de.seuhd.campuscoffee.data.persistence.repositories.ExpenseRepository
import de.seuhd.campuscoffee.domain.model.Expense
import de.seuhd.campuscoffee.domain.ports.data.ExpenseDataService
import org.springframework.stereotype.Service
import java.util.UUID

/** Data adapter for expenses. */
@Service
class ExpenseDataServiceImpl(
    repository: ExpenseRepository,
    entityMapper: ExpenseEntityMapper
) : CrudDataServiceImpl<Expense, ExpenseEntity, ExpenseRepository, UUID>(
        repository,
        entityMapper,
        Expense::class.java
    ),
    ExpenseDataService
