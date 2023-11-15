package com.example.astontest.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoneyTransferDto extends AbstractTransactionDto {
    private String accountNameFrom;
    private String accountNameTo;
}
