package com.example.astontest.user.mapper;

import com.example.astontest.user.dto.AccountDto;
import com.example.astontest.user.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountEntityToDtoMapper {
    AccountDto toDto(Account account);
}
