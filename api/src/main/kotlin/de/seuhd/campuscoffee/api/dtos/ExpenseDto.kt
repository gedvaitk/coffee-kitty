package de.seuhd.campuscoffee.api.dtos

import de.seuhd.campuscoffee.domain.model.Expense
import de.seuhd.campuscoffee.domain.model.ExpenseKind
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/** Request and response body for a recorded expense. */
data class ExpenseDto(
    @field:Schema(accessMode = Schema.AccessMode.READ_ONLY)
    override val id: UUID? = null,
    @field:Schema(accessMode = Schema.AccessMode.READ_ONLY)
    val createdAt: LocalDateTime? = null,
    @field:Schema(accessMode = Schema.AccessMode.READ_ONLY)
    val updatedAt: LocalDateTime? = null,
    @field:NotNull
    @field:DecimalMin("0.01")
    @field:DecimalMax("1000.00")
    @field:Digits(integer = 4, fraction = 2)
    val amount: BigDecimal?,
    @field:NotNull
    val kind: ExpenseKind?,
    @field:Size(max = 255)
    val itemName: String? = null,
    @field:Size(max = 255)
    val beanName: String? = null,
    val weightGrams: Int? = null,
    @field:Size(max = 500)
    val note: String? = null
) : Dto<UUID>
