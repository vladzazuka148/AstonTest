package com.example.astontest.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "internal_operation")
@Getter
@Setter
public class InternalOperation extends AbstractTransaction {
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private Account account;
}
