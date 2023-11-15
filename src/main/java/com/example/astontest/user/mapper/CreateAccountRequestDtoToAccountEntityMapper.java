package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.CreateAccountRequestDto;
import com.example.astontest.user.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateAccountRequestDtoToAccountEntityMapper {
    Account toBankAccountEntity(CreateAccountRequestDto createAccountRequestDto);
}
