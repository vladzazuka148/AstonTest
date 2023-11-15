package com.example.astontest.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountDto {
    private String name;
    private BigDecimal money;
}
