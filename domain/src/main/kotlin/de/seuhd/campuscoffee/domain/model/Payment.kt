package de.seuhd.campuscoffee.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/** A deposit received for a user and added to that user's balance. */
data class Payment(
    override val id: UUID? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val user: User,
    val amount: BigDecimal,
    val note: String? = null
) : DomainModel<UUID>
