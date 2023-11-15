package com.example.astontest.user.repository;

import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.InternalOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с операциями в рамках одного счета
 */
public interface InternalOperationRepository extends JpaRepository<InternalOperation, String> {
    /**
     * Получить все внутренние операции по счету
     */
    List<InternalOperation> findAllByAccount(Account account);
}
