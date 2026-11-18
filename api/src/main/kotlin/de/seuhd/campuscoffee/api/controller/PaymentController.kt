package de.seuhd.campuscoffee.api.controller

import de.seuhd.campuscoffee.api.dtos.PaymentDto
import de.seuhd.campuscoffee.api.mapper.PaymentDtoMapper
import de.seuhd.campuscoffee.domain.model.Payment
import de.seuhd.campuscoffee.domain.ports.api.PaymentService
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

/** Controller for recording payments for users. */
@RestController
@RequestMapping("/users/{userId}/payments")
class PaymentController(
    private val paymentService: PaymentService,
    private val userService: UserService,
    private val mapper: PaymentDtoMapper
) {
    /**
     * Records a payment for [userId] and increases that user's balance.
     *
     * @param userId the user receiving the payment
     * @param dto the payment details
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(
        @PathVariable userId: UUID,
        @RequestBody @Valid dto: PaymentDto
    ): ResponseEntity<PaymentDto> {
        val user = userService.getById(userId)
        val created =
            paymentService.create(
                Payment(
                    user = user,
                    amount = requireNotNull(dto.amount),
                    note = dto.note
                )
            )
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.fromDomain(created))
    }
}
