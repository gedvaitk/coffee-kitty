package de.seuhd.campuscoffee.api.mapper

import de.seuhd.campuscoffee.api.dtos.ExpenseDto
import de.seuhd.campuscoffee.domain.model.Expense
import org.mapstruct.Mapper

/** MapStruct mapper for expenses. */
@Mapper(componentModel = "spring")
interface ExpenseDtoMapper {
    /**
     * Converts an expense to its response DTO.
     *
     * @param source the expense
     */
    fun fromDomain(source: Expense): ExpenseDto
}
