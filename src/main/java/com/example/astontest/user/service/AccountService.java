package com.example.astontest.user.service;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.user.entity.Account;

import java.math.BigDecimal;

/**
 * Сервис для создания, сохранения и обновления банковских счетов
 */
public interface AccountService {
    /**
     * Создать банковский счет
     *
     * @param entity сформированная для сохранения сущность нового счета
     */
    ResponseResult createAccount(Account entity);

    /**
     * Пополнить счет
     *
     * @param account            банковский счет
     * @param accountEntityMoney сумма денег сущности из базы данных
     * @param requestDtoMoney    сумма денег из запроса для пополнения
     */
    ResponseResult depositMoney(BigDecimal requestDtoMoney,
                                BigDecimal accountEntityMoney,
                                Account account);

    /**
     * Вывести деньги со счета
     *
     * @param account            банковский счет
     * @param accountEntityMoney сумма денег сущности из базы данных
     * @param requestDtoMoney    сумма денег из запроса для снятия
     */
    ResponseResult withdrawMoney(Account account,
                                 BigDecimal accountEntityMoney,
                                 BigDecimal requestDtoMoney);

    /**
     * Вывести деньги со счета
     *
     * @param from            банковский счет c которого производиться списание средств
     * @param to              банковский счет куда зачисляются деньги
     * @param fromMoney       баланс счета с которого производиться списание
     * @param toMoney         баланс счета куда зачисляются деньги
     * @param requestDtoMoney сумма денег из на которую должен пополниться счет
     */
    ResponseResult transferMoney(Account from,
                                 Account to,
                                 BigDecimal fromMoney,
                                 BigDecimal toMoney,
                                 BigDecimal requestDtoMoney);
}
