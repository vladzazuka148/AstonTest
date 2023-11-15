package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.InternalOperationDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.InternalOperation;
import com.example.astontest.user.entity.enums.OperationType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalOperationEntityToDtoMapperTest {
    InternalOperationEntityToDtoMapper mapper =
            new InternalOperationEntityToDtoMapperImpl();
    @Test
    void toDto() {
        //GIVEN
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        String expectedName = "name";
        BigDecimal expectedMoney = new BigDecimal(12312);

        Account account = new Account();
        account.setName(expectedName);

        InternalOperation internalOperation = new InternalOperation();
        internalOperation.setMoney(expectedMoney);
        internalOperation.setOperationType(OperationType.DEPOSIT);
        internalOperation.setTransactionDataTime(expectedLocalDateTime);
        internalOperation.setAccount(account);

        //WHEN
        InternalOperationDto actualInternalOperationDto = mapper.toDto(internalOperation);

        //THEN
        assertEquals(expectedName, actualInternalOperationDto.getName());
        assertEquals(expectedLocalDateTime, actualInternalOperationDto.getTransactionDataTime());
        assertEquals(expectedMoney, actualInternalOperationDto.getMoney());
        assertEquals(expectedName, actualInternalOperationDto.getName());
    }
}