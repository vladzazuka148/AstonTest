package com.example.astontest.user.entity;

import com.example.astontest.user.entity.enums.OperationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "transactional")
@Getter
@Setter
public abstract class AbstractTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "data_time")
    private LocalDateTime transactionDataTime;

    @Column(name = "money")
    private BigDecimal money;

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
}
