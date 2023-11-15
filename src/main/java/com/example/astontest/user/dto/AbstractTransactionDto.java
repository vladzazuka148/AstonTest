package com.example.astontest.user.dto;

import com.example.astontest.user.entity.enums.OperationType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class AbstractTransactionDto {
    private LocalDateTime transactionDataTime;
    private BigDecimal money;
    private OperationType operationType;
}
