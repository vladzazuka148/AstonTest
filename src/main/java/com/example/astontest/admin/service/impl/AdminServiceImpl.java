package com.example.astontest.admin.service.impl;

import com.example.astontest.admin.service.AdminService;
import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import com.example.astontest.user.dto.AbstractTransactionDto;
import com.example.astontest.user.dto.AccountDto;
import com.example.astontest.user.dto.MoneyTransferDto;
import com.example.astontest.user.dto.InternalOperationDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.mapper.AccountEntityToDtoMapper;
import com.example.astontest.user.mapper.InternalOperationEntityToDtoMapper;
import com.example.astontest.user.mapper.MoneyTransferEntityToDto;
import com.example.astontest.user.repository.AccountRepository;
import com.example.astontest.user.repository.MoneyTransferRepository;
import com.example.astontest.user.repository.InternalOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    public static final String ALL_ACCOUNTS_INFO = "Данные по всем счетам";
    public static final String NO_SUCH_ACCOUNT = "Такого счета не существует";
    public static final String ALL_TRANSACTIONS_BY_ACCOUNT_NAME_INFO = "Все транзакции по имени счета";

    private final AccountRepository accountRepository;
    private final InternalOperationRepository internalOperationRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final MoneyTransferEntityToDto moneyTransferEntityToDto;
    private final InternalOperationEntityToDtoMapper internalOperationEntityToDtoMapper;
    private final AccountEntityToDtoMapper accountEntityToDtoMapper;

    @Override
    public ResponseResult getAllAccounts() {
        List<AccountDto> accountDtoList = getAccountDtos();
        return new ResponseResult(ALL_ACCOUNTS_INFO, ResponseStatus.SUCCESS, accountDtoList);
    }

    @Override
    public ResponseResult getAllTransactionByAccount(String accountName) {
        Account account = accountRepository.findAccountEntityByName(accountName)
                .orElse(null);

        if (account == null) {
            return new ResponseResult(NO_SUCH_ACCOUNT, ResponseStatus.ERROR, null);
        }

        List<AbstractTransactionDto> abstractTransactionDtos = getAllTransactionsByAccount(account);

        return new ResponseResult(ALL_TRANSACTIONS_BY_ACCOUNT_NAME_INFO,
                ResponseStatus.SUCCESS,
                abstractTransactionDtos);

    }

    private List<AbstractTransactionDto> getAllTransactionsByAccount(Account account) {
        List<AbstractTransactionDto> abstractTransactionDtos = new ArrayList<>();
        List<MoneyTransferDto> moneyTransfersByFromOrTo =
                getMoneyTransfersByFromOrTo(account);

        List<InternalOperationDto> internalOperationDtos =
                getInternalOperationDtosByAccount(account);

        abstractTransactionDtos.addAll(moneyTransfersByFromOrTo);
        abstractTransactionDtos.addAll(internalOperationDtos);
        return abstractTransactionDtos;
    }

    private List<InternalOperationDto> getInternalOperationDtosByAccount(Account account) {
        return internalOperationRepository.findAllByAccount(account)
                .stream()
                .map(internalOperationEntityToDtoMapper::toDto)
                .toList();
    }

    private List<MoneyTransferDto> getMoneyTransfersByFromOrTo(Account account) {
        return moneyTransferRepository.findDistinctByFromOrTo(account, account)
                .stream()
                .map(moneyTransferEntityToDto::toDto)
                .toList();
    }

    private List<AccountDto> getAccountDtos() {
        return accountRepository.findAll()
                .stream()
                .map(accountEntityToDtoMapper::toDto)
                .toList();
    }
}
