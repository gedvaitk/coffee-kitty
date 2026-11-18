package de.seuhd.campuscoffee.api.controller

import de.seuhd.campuscoffee.api.dtos.BalanceDto
import de.seuhd.campuscoffee.domain.ports.api.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/** Controller under `/api/users/{userId}/balance` that reads a user's balance. */
@Tag(name = "Balance", description = "Reading a user's balance.")
@RestController
@RequestMapping("/users/{userId}/balance")
class BalanceController(
    private val userService: UserService
) {
    /**
     * Returns the balance of the user with [userId].
     *
     * @param userId the id of the user
     * @return the login name and balance of the user
     */
    @Operation(summary = "Get a user's balance.")
    @GetMapping("")
    fun get(
        @Parameter(description = "Unique identifier of the user.", required = true)
        @PathVariable userId: UUID
    ): ResponseEntity<BalanceDto> {
        val user = userService.getById(userId)
        return ResponseEntity.ok(BalanceDto(loginName = user.loginName, balance = user.balance))
    }
}
