package de.seuhd.campuscoffee.api.controller

import de.seuhd.campuscoffee.api.dtos.ExpenseDto
import de.seuhd.campuscoffee.api.mapper.ExpenseDtoMapper
import de.seuhd.campuscoffee.domain.model.Expense
import de.seuhd.campuscoffee.domain.ports.api.ExpenseService
import de.seuhd.campuscoffee.domain.ports.api.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** Controller for recording expenses for users. */
@RestController
@RequestMapping("/users/{userId}/expenses")
class ExpenseController(
    private val expenseService: ExpenseService,
    private val userService: UserService,
    private val mapper: ExpenseDtoMapper
) {
    /**
     * Records an expense for [userId] and increases that user's balance.
     *
     * @param userId the user who paid for the expense
     * @param dto the expense details
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(
        @PathVariable userId: UUID,
        @RequestBody @Valid dto: ExpenseDto
    ): ResponseEntity<ExpenseDto> {
        val user = userService.getById(userId)
        val created =
            expenseService.create(
                Expense(
                    user = user,
                    amount = requireNotNull(dto.amount),
                    kind = requireNotNull(dto.kind),
                    itemName = dto.itemName,
                    beanName = dto.beanName,
                    weightGrams = dto.weightGrams,
                    note = dto.note
                )
            )
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.fromDomain(created))
    }
}
