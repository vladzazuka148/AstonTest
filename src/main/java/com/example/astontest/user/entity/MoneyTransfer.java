package com.example.astontest.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "money_transfer")
@Getter
@Setter
public class MoneyTransfer extends AbstractTransaction {
    @ManyToOne
    @JoinColumn(name = "account_from")
    private Account from;

    @ManyToOne
    @JoinColumn(name = "account_to")
    private Account to;
}
