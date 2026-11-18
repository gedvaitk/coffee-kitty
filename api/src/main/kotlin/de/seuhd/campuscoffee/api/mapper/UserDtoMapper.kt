package de.seuhd.campuscoffee.api.mapper

import de.seuhd.campuscoffee.api.dtos.UserDto
import de.seuhd.campuscoffee.domain.model.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * MapStruct mapper between a [User] and a [UserDto]. The mapper declares no `@Mapping`, because every property has the
 * same name in both classes. If one class gets a property that the other class does not have, MapStruct stops the
 * build with an error, because the kapt convention plugin sets `mapstruct.unmappedTargetPolicy` to `ERROR`.
 */
@Mapper(componentModel = "spring")
interface UserDtoMapper : DtoMapper<User, UserDto> {
    /**
     * Converts a user DTO to a domain user.
     *
     * Balance is owned by a dedicated balance endpoint and is not writable through user DTOs.
     *
     * @param source the user DTO
     */
    @Mapping(target = "balance", expression = "java(java.math.BigDecimal.ZERO)")
    override fun toDomain(source: UserDto): User
}
