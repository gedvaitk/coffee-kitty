package de.seuhd.campuscoffee.data.persistence.repositories

import de.seuhd.campuscoffee.data.persistence.entities.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/** Spring Data repository for payments. */
interface PaymentRepository : JpaRepository<PaymentEntity, UUID>
