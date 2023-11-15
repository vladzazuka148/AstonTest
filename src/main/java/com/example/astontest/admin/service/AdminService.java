package com.example.astontest.admin.service;

import com.example.astontest.core.dto.responsedto.ResponseResult;

/**
 * Сервис для обработки запросов для администрации
 */
public interface AdminService {
    /**
     * получить данные обо всех счетах
     */
    ResponseResult getAllAccounts();

    /**
     * получить все транзакции по имени счета
     *
     * @param accountName имя счета
     */
    ResponseResult getAllTransactionByAccount(String accountName);
}
