package com.example.astontest.user.repository;

import com.example.astontest.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы со счетами
 */
public interface AccountRepository extends JpaRepository<Account, String> {
    /**
     * Получить счет по собственному имени
     */
    Optional<Account> findAccountEntityByName(String name);
}
