package de.seuhd.campuscoffee.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/** A purchase made for coffee-related supplies and added to the buyer's balance. */
data class Expense(
    override val id: UUID? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val user: User,
    val amount: BigDecimal,
    val kind: ExpenseKind,
    val itemName: String? = null,
    val beanName: String? = null,
    val weightGrams: Int? = null,
    val note: String? = null
) : DomainModel<UUID>

/** The kind of item bought in an expense. */
enum class ExpenseKind {
    COFFEE,
    MILK,
    SUGAR,
    FILTER,
    OTHER
}
