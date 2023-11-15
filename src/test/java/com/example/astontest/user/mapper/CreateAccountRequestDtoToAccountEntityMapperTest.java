package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.CreateAccountRequestDto;
import com.example.astontest.user.entity.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateAccountRequestDtoToAccountEntityMapperTest {
    private final CreateAccountRequestDtoToAccountEntityMapper mapper =
            new CreateAccountRequestDtoToAccountEntityMapperImpl();
    @Test
    void toBankAccountEntity() {
        //GIVEN
        String expectedName = "name";
        String expectedPincode = "1421";

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setName(expectedName);
        createAccountRequestDto.setPincode(expectedPincode);

        //WHEN
        Account actualAccount = mapper.toBankAccountEntity(createAccountRequestDto);

        //THEN
        assertEquals(expectedName, actualAccount.getName());
        assertEquals(expectedPincode, actualAccount.getPincode());
        assertNull(actualAccount.getId());
        assertNull(actualAccount.getMoney());
    }
}