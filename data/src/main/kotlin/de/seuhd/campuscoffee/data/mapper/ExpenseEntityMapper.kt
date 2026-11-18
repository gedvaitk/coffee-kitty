package de.seuhd.campuscoffee.data.mapper

import de.seuhd.campuscoffee.data.persistence.entities.ExpenseEntity
import de.seuhd.campuscoffee.domain.model.Expense
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

/** MapStruct mapper for expenses. */
@Mapper(componentModel = "spring", uses = [UserEntityMapper::class, EntityReferenceResolver::class])
interface ExpenseEntityMapper : EntityMapper<Expense, ExpenseEntity> {
    @Mapping(target = "user", source = "user.id")
    override fun toEntity(source: Expense): ExpenseEntity

    @Mapping(target = "user", source = "user.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    override fun updateEntity(
        source: Expense,
        @MappingTarget target: ExpenseEntity
    )
}
