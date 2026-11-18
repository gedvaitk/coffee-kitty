package de.seuhd.campuscoffee.data.persistence.entities

import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

/** JPA entity for a recorded payment. */
@jakarta.persistence.Entity
@Table(name = "payments")
class PaymentEntity : Entity() {
    @field:ManyToOne
    @field:JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @field:Column(nullable = false, precision = 12, scale = 2)
    var amount: BigDecimal? = null

    @field:Column(length = 500)
    var note: String? = null
}
