package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.InternalOperationDto;
import com.example.astontest.user.entity.InternalOperation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InternalOperationEntityToDtoMapper {
    @Mapping(source = "transactionDataTime", target = "transactionDataTime")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "operationType", target = "operationType")
    @Mapping(source = "account.id", target = "name")
    InternalOperationDto toDto(InternalOperation internalOperation);
}