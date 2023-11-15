package com.example.astontest.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferMoneyRequestDto {
    private String accountNameFrom;
    private String accountNameTo;
    private String pincode;
    private BigDecimal money;
}
