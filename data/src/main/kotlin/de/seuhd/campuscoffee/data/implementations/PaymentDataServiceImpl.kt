package de.seuhd.campuscoffee.data.implementations

import de.seuhd.campuscoffee.data.mapper.PaymentEntityMapper
import de.seuhd.campuscoffee.data.persistence.entities.PaymentEntity
import de.seuhd.campuscoffee.data.persistence.repositories.PaymentRepository
import de.seuhd.campuscoffee.domain.model.Payment
import de.seuhd.campuscoffee.domain.ports.data.PaymentDataService
import org.springframework.stereotype.Service
import java.util.UUID

/** Data adapter for payments. */
@Service
class PaymentDataServiceImpl(
    repository: PaymentRepository,
    entityMapper: PaymentEntityMapper
) : CrudDataServiceImpl<Payment, PaymentEntity, PaymentRepository, UUID>(
        repository,
        entityMapper,
        Payment::class.java
    ),
    PaymentDataService
