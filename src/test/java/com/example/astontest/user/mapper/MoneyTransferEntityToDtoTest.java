package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.MoneyTransferDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.MoneyTransfer;
import com.example.astontest.user.entity.enums.OperationType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferEntityToDtoTest {
    private final MoneyTransferEntityToDtoMapper mapper =
            new MoneyTransferEntityToDtoMapperImpl();

    @Test
    void toDto() {
        //GIVEN
        String expectedNameTo = "to";
        String expectedNameFrom = "from";
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        BigDecimal expectedMoney = new BigDecimal(12341);

        Account to = new Account();
        to.setName(expectedNameTo);

        Account from = new Account();
        from.setName(expectedNameFrom);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setFrom(from);
        moneyTransfer.setTo(to);
        moneyTransfer.setOperationType(OperationType.TRANSFER);
        moneyTransfer.setTransactionDataTime(expectedLocalDateTime);
        moneyTransfer.setMoney(expectedMoney);

        //WHEN
        MoneyTransferDto moneyTransferDto = mapper.toDto(moneyTransfer);

        //THEN
        assertEquals(expectedNameTo, moneyTransferDto.getAccountNameTo());
        assertEquals(expectedNameFrom, moneyTransferDto.getAccountNameFrom());
        assertEquals(expectedMoney, moneyTransferDto.getMoney());
        assertEquals(expectedLocalDateTime, moneyTransferDto.getTransactionDataTime());
        assertEquals(OperationType.TRANSFER, moneyTransferDto.getOperationType());
    }
}