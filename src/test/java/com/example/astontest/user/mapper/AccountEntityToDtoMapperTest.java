package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.AccountDto;
import com.example.astontest.user.entity.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountEntityToDtoMapperTest {
    private final AccountEntityToDtoMapperImpl accountEntityToDtoMapper =
            new AccountEntityToDtoMapperImpl();

    @Test
    void toDto() {
        //GIVEN
        BigDecimal expectedMoney = new BigDecimal(1231);
        String expectedName = "name";

        Account account = new Account();
        account.setMoney(expectedMoney);
        account.setName(expectedName);
        account.setId("12412412412");
        account.setPincode("1241");

        //WHEN
        AccountDto accountDto = accountEntityToDtoMapper.toDto(account);

        //THEN
        assertEquals(expectedName,accountDto.getName());
        assertEquals(expectedMoney,accountDto.getMoney());
    }
}