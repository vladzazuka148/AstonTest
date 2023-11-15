package com.example.astontest.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WithdrawMoneyRequestDto {
    private String name;
    private String pincode;
    private BigDecimal money;
}
