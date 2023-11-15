package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.MoneyTransferDto;
import com.example.astontest.user.entity.MoneyTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MoneyTransferEntityToDto {
    @Mapping(source = "transactionDataTime", target = "transactionDataTime")
    @Mapping(source = "money", target = "money")
    @Mapping(source = "operationType", target = "operationType")
    @Mapping(source = "from.name", target = "accountNameFrom")
    @Mapping(source = "to.name", target = "accountNameTo")
    MoneyTransferDto toDto(MoneyTransfer moneyTransfer);
}