package com.example.astontest.user.repository;

import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с переводами денег
 */
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, String> {
    /**
     * Получить уникальные трансферы по аккаунту с которого списываются
     * и на который начисляются средства
     *
     * @param accountFrom счет с которого списываются средства
     * @param accountTo   счет на который зачисляются средства
     */
    List<MoneyTransfer> findDistinctByFromOrTo(Account accountFrom, Account accountTo);
}
