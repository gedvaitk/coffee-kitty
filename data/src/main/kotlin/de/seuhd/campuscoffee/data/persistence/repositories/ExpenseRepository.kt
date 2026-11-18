package de.seuhd.campuscoffee.data.persistence.repositories

import de.seuhd.campuscoffee.data.persistence.entities.ExpenseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/** Spring Data repository for expenses. */
interface ExpenseRepository : JpaRepository<ExpenseEntity, UUID>
