package com.example.astontest.user.service.impl;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.InternalOperation;
import com.example.astontest.user.entity.MoneyTransfer;
import com.example.astontest.user.entity.enums.OperationType;
import com.example.astontest.user.repository.AccountRepository;
import com.example.astontest.user.repository.InternalOperationRepository;
import com.example.astontest.user.repository.MoneyTransferRepository;
import com.example.astontest.user.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.example.astontest.user.facade.impl.AccountFacadeImpl.BANK_ACCOUNT_EXIST_WITH_NAME;
import static com.example.astontest.user.service.impl.AccountServiceImpl.CREATE_BANK_ACCOUNT_SUCCESS;
import static com.example.astontest.user.service.impl.AccountServiceImpl.OPERATION_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MoneyTransferRepository moneyTransferRepository;
    @Mock
    private InternalOperationRepository internalOperationRepository;
    @Captor
    private ArgumentCaptor<InternalOperation> internalOperationArgumentCaptor;
    @Captor
    private ArgumentCaptor<MoneyTransfer> moneyTransferArgumentCaptor;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount() {
        //GIVEN
        Account account = new Account();

        //WHEN
        ResponseResult result = accountService.createAccount(account);

        //THEN
        assertEquals(new BigDecimal(0), account.getMoney());
        verify(accountRepository).save(account);
        assertNull(result.getBody());
        assertEquals(CREATE_BANK_ACCOUNT_SUCCESS, result.getMessage());
        assertEquals(ResponseStatus.SUCCESS, result.getStatus());
    }

    @Test
    void depositMoney() {
        //GIVEN
        BigDecimal requestDtoMoney = new BigDecimal(1231);
        BigDecimal accountEntityMoney = new BigDecimal(1325);

        Account account = new Account();

        //WHEN
        ResponseResult result = accountService.depositMoney(requestDtoMoney,
                accountEntityMoney,
                account);

        //THEN
        verify(accountRepository).save(account);
        verify(internalOperationRepository).save(internalOperationArgumentCaptor.capture());
        assertEquals(account, internalOperationArgumentCaptor.getValue().getAccount());
        assertEquals(OperationType.DEPOSIT, internalOperationArgumentCaptor.getValue().getOperationType());
        assertEquals(requestDtoMoney, internalOperationArgumentCaptor.getValue().getMoney());
        assertNull(result.getBody());
        assertEquals(OPERATION_SUCCESS + account.getMoney(), result.getMessage());
        assertEquals(ResponseStatus.SUCCESS, result.getStatus());

    }

    @Test
    void withdrawMoney() {
        //GIVEN
        BigDecimal requestDtoMoney = new BigDecimal(1231);
        BigDecimal accountEntityMoney = new BigDecimal(1325);

        Account account = new Account();

        //WHEN
        ResponseResult result = accountService.withdrawMoney(account,
                accountEntityMoney,
                requestDtoMoney);

        //THEN
        verify(accountRepository).save(account);
        verify(internalOperationRepository).save(internalOperationArgumentCaptor.capture());
        assertEquals(account, internalOperationArgumentCaptor.getValue().getAccount());
        assertEquals(OperationType.WITHDRAW, internalOperationArgumentCaptor.getValue().getOperationType());
        assertEquals(requestDtoMoney, internalOperationArgumentCaptor.getValue().getMoney());
        assertNull(result.getBody());
        assertEquals(OPERATION_SUCCESS + account.getMoney(), result.getMessage());
        assertEquals(ResponseStatus.SUCCESS, result.getStatus());

    }

    @Test
    void transferMoney() {
        //GIVEN
        Account from = new Account();
        Account to = new Account();
        BigDecimal fromMoney = new BigDecimal(123);
        BigDecimal toMoney = new BigDecimal(124125);
        BigDecimal requestDtoMoney = new BigDecimal(1525);

        BigDecimal expectedFromMoney = fromMoney.subtract(requestDtoMoney);
        BigDecimal expectedToMoney = toMoney.add(requestDtoMoney);

        //WHEN
        accountService.transferMoney(from,
                to,
                fromMoney,
                toMoney,
                requestDtoMoney);

        //THEN
        assertEquals(expectedFromMoney, from.getMoney());
        assertEquals(expectedToMoney, to.getMoney());
        verify(accountRepository).save(from);
        verify(accountRepository).save(to);
        verify(moneyTransferRepository).save(moneyTransferArgumentCaptor.capture());
        assertEquals(requestDtoMoney, moneyTransferArgumentCaptor.getValue().getMoney());
        assertEquals(OperationType.TRANSFER, moneyTransferArgumentCaptor.getValue().getOperationType());
        assertEquals(from, moneyTransferArgumentCaptor.getValue().getFrom());
        assertEquals(to, moneyTransferArgumentCaptor.getValue().getTo());
    }
}