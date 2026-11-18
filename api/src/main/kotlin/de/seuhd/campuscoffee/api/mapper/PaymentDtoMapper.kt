package de.seuhd.campuscoffee.api.mapper

import de.seuhd.campuscoffee.api.dtos.PaymentDto
import de.seuhd.campuscoffee.domain.model.Payment
import org.mapstruct.Mapper

/** MapStruct mapper for payments. */
@Mapper(componentModel = "spring")
interface PaymentDtoMapper {
    /**
     * Converts a payment to its response DTO.
     *
     * @param source the payment
     */
    fun fromDomain(source: Payment): PaymentDto
}
