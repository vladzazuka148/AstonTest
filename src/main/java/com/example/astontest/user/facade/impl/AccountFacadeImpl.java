package com.example.astontest.user.facade.impl;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import com.example.astontest.user.dto.CreateAccountRequestDto;
import com.example.astontest.user.dto.DepositMoneyRequestDto;
import com.example.astontest.user.dto.TransferMoneyRequestDto;
import com.example.astontest.user.dto.WithdrawMoneyRequestDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.facade.AccountFacade;
import com.example.astontest.user.mapper.CreateAccountRequestDtoToAccountEntityMapper;
import com.example.astontest.user.repository.AccountRepository;
import com.example.astontest.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AccountFacadeImpl implements AccountFacade {
    public static final String BANK_ACCOUNT_EXIST_WITH_NAME = " Cчет с таким именем уже существует. name: ";
    public static final String INCORRECT_PINCODE = "Неверный пин-код";
    public static final String NOT_ENOUGHT_MONEY = "Недостаточно средств";
    public static final String NO_SUCH_ACCOUNT = "Нет данного счета для совершения операции.";
    public static final String INVALID_COUNT_OF_CHARS_FOR_PINCODE = "Невалидное количество символов для пинкода";

    private final AccountRepository accountRepository;
    private final CreateAccountRequestDtoToAccountEntityMapper createAccountRequestDtoToAccountEntityMapper;
    private final AccountService accountService;

    @Override
    public ResponseResult createAccount(CreateAccountRequestDto createAccountRequestDto) {
        if (AccountExist(createAccountRequestDto)) {
            return new ResponseResult(BANK_ACCOUNT_EXIST_WITH_NAME +
                    createAccountRequestDto.getName(),
                    ResponseStatus.ERROR,
                    null);
        }
        if (isNotValidPincode(createAccountRequestDto)) {
            return new ResponseResult(INVALID_COUNT_OF_CHARS_FOR_PINCODE,
                    ResponseStatus.ERROR,
                    null);
        }

        Account account = createAccountRequestDtoToAccountEntityMapper.toBankAccountEntity(createAccountRequestDto);
        return accountService.createAccount(account);
    }

    @Override
    public ResponseResult depositMoney(DepositMoneyRequestDto depositMoneyRequestDto) {
        String accountName = depositMoneyRequestDto.getName();

        Account account = getAccountByName(accountName);

        if (account == null) {
            return new ResponseResult(NO_SUCH_ACCOUNT,
                    ResponseStatus.ERROR,
                    null);
        }

        BigDecimal requestDtoMoney = depositMoneyRequestDto.getMoney();
        BigDecimal accountMoney = account.getMoney();
        return accountService.depositMoney(requestDtoMoney,
                accountMoney,
                account);
    }

    @Override
    public ResponseResult withdrawMoney(WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
        String accountName = withdrawMoneyRequestDto.getName();

        Account account = getAccountByName(accountName);

        if (account == null) {
            return new ResponseResult(NO_SUCH_ACCOUNT,
                    ResponseStatus.ERROR,
                    null);
        }
        if (isNotValidPincode(withdrawMoneyRequestDto.getPincode(), account)) {
            return new ResponseResult(INCORRECT_PINCODE,
                    ResponseStatus.ERROR,
                    null);
        }
        BigDecimal accountMoney = account.getMoney();

        BigDecimal requestDtoMoney = withdrawMoneyRequestDto.getMoney();

        if (accountMoney.compareTo(requestDtoMoney) < 0) {
            return new ResponseResult(NOT_ENOUGHT_MONEY,
                    ResponseStatus.ERROR,
                    null);
        }
        return accountService.withdrawMoney(account,
                accountMoney,
                requestDtoMoney);
    }

    @Override
    public ResponseResult transferMoney(TransferMoneyRequestDto transferMoneyRequestDto) {
        Account from = getAccountByName(transferMoneyRequestDto.getAccountNameFrom());
        Account to = getAccountByName(transferMoneyRequestDto.getAccountNameTo());
        if (accountsForTransactionIsNull(from, to)) {
            return new ResponseResult(NO_SUCH_ACCOUNT,
                    ResponseStatus.ERROR,
                    null);
        }
        if (isNotValidPincode(transferMoneyRequestDto.getPincode(), from)) {
            return new ResponseResult(INCORRECT_PINCODE,
                    ResponseStatus.ERROR,
                    null);
        }
        BigDecimal requestDtoMoney = transferMoneyRequestDto.getMoney();

        BigDecimal fromMoney = from.getMoney();
        BigDecimal toMoney = to.getMoney();

        if (fromMoney.compareTo(requestDtoMoney) < 0) {
            return new ResponseResult(NOT_ENOUGHT_MONEY,
                    ResponseStatus.ERROR,
                    null);
        }
        return accountService.transferMoney(from,
                to,
                fromMoney,
                toMoney,
                requestDtoMoney);
    }

    private static boolean isNotValidPincode(CreateAccountRequestDto createAccountRequestDto) {
        return createAccountRequestDto.getPincode().toCharArray().length != 4;
    }

    private static boolean accountsForTransactionIsNull(Account from, Account to) {
        return from == null || to == null;
    }

    private static boolean isNotValidPincode(String pincode, Account account) {
        return !pincode.equals(account.getPincode());
    }

    private Account getAccountByName(String accountName) {
        return accountRepository.findAccountEntityByName(accountName)
                .orElse(null);
    }

    private boolean AccountExist(CreateAccountRequestDto createAccountRequestDto) {
        return accountRepository.findAccountEntityByName(createAccountRequestDto.getName()).isPresent();
    }
}
