package de.seuhd.campuscoffee.data.persistence.entities

import de.seuhd.campuscoffee.domain.model.ExpenseKind
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

/** JPA entity for a recorded expense. */
@jakarta.persistence.Entity
@Table(name = "expenses")
class ExpenseEntity : Entity() {
    @field:ManyToOne
    @field:JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity? = null

    @field:Column(nullable = false, precision = 12, scale = 2)
    var amount: BigDecimal? = null

    @field:Enumerated(EnumType.STRING)
    @field:Column(nullable = false, length = 20)
    var kind: ExpenseKind? = null

    @field:Column(name = "item_name", length = 255)
    var itemName: String? = null

    @field:Column(name = "bean_name", length = 255)
    var beanName: String? = null

    @field:Column(name = "weight_grams")
    var weightGrams: Int? = null

    @field:Column(length = 500)
    var note: String? = null
}
