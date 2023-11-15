package com.example.astontest.user.facade;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import com.example.astontest.user.dto.CreateAccountRequestDto;
import com.example.astontest.user.dto.DepositMoneyRequestDto;
import com.example.astontest.user.dto.TransferMoneyRequestDto;
import com.example.astontest.user.dto.WithdrawMoneyRequestDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.mapper.CreateAccountRequestDtoToAccountEntityMapper;
import com.example.astontest.user.repository.AccountRepository;
import com.example.astontest.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Фасад для валидации и сквозной логики для работы со счетами в {@link AccountService}
 */
public interface AccountFacade {
    /**
     * Создать банковский счет
     *
     * @param createAccountRequestDto данные из запроса для выполнения операции
     */
    ResponseResult createAccount(CreateAccountRequestDto createAccountRequestDto);

    /**
     * Создать банковский счет
     *
     * @param depositMoneyRequestDto данные из запроса для выполнения операции
     */
    ResponseResult depositMoney(DepositMoneyRequestDto depositMoneyRequestDto);

    /**
     * Создать банковский счет
     *
     * @param withdrawMoneyRequestDto данные из запроса для выполнения операции
     */
    ResponseResult withdrawMoney(WithdrawMoneyRequestDto withdrawMoneyRequestDto);

    /**
     * Создать банковский счет
     *
     * @param transferMoneyRequestDto данные из запроса для выполнения операции
     */
    ResponseResult transferMoney(TransferMoneyRequestDto transferMoneyRequestDto);

}
