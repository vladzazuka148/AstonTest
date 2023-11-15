package com.example.astontest.admin.service.impl;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import com.example.astontest.user.dto.AbstractTransactionDto;
import com.example.astontest.user.dto.AccountDto;
import com.example.astontest.user.dto.InternalOperationDto;
import com.example.astontest.user.dto.MoneyTransferDto;
import com.example.astontest.user.entity.Account;
import com.example.astontest.user.entity.InternalOperation;
import com.example.astontest.user.entity.MoneyTransfer;
import com.example.astontest.user.entity.enums.OperationType;
import com.example.astontest.user.mapper.AccountEntityToDtoMapper;
import com.example.astontest.user.mapper.InternalOperationEntityToDtoMapper;
import com.example.astontest.user.mapper.MoneyTransferEntityToDto;
import com.example.astontest.user.repository.AccountRepository;
import com.example.astontest.user.repository.InternalOperationRepository;
import com.example.astontest.user.repository.MoneyTransferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.astontest.admin.service.impl.AdminServiceImpl.ALL_ACCOUNTS_INFO;
import static com.example.astontest.admin.service.impl.AdminServiceImpl.ALL_TRANSACTIONS_BY_ACCOUNT_NAME_INFO;
import static com.example.astontest.admin.service.impl.AdminServiceImpl.NO_SUCH_ACCOUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private InternalOperationRepository internalOperationRepository;
    @Mock
    private MoneyTransferRepository moneyTransferRepository;
    @Mock
    private MoneyTransferEntityToDto moneyTransferEntityToDto;
    @Mock
    private InternalOperationEntityToDtoMapper internalOperationEntityToDtoMapper;
    @Mock
    private AccountEntityToDtoMapper accountEntityToDtoMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void getAllAccounts() {
        //GIVEN
        Account account = new Account();
        account.setMoney(new BigDecimal(123));
        account.setName("name");
        account.setPincode("1242");

        AccountDto accountDto = new AccountDto();
        accountDto.setMoney(new BigDecimal(124));
        accountDto.setName("name");

        List<Account> accountList = new ArrayList<>();
        accountList.add(account);

        List<AccountDto> expectedAccountDtoList = new ArrayList<>();
        expectedAccountDtoList.add(accountDto);

        when(accountRepository.findAll()).thenReturn(accountList);
        when(accountEntityToDtoMapper.toDto(account)).thenReturn(accountDto);

        //WHEN
        ResponseResult responseResult = adminService.getAllAccounts();

        //THEN
        assertEquals(ResponseStatus.SUCCESS, responseResult.getStatus());
        assertEquals(ALL_ACCOUNTS_INFO, responseResult.getMessage());
        assertEquals(expectedAccountDtoList, responseResult.getBody());
    }

    @Test
    void getAllTransactionalByAccount() {
        //GIVEN
        String accountName = "accountName";

        Account account = new Account();
        account.setPincode("1241");
        account.setMoney(new BigDecimal(1212));
        account.setName(accountName);

        InternalOperation internalOperation = new InternalOperation();

        InternalOperationDto internalOperationDto = new InternalOperationDto();
        internalOperationDto.setName("dtoName");
        internalOperationDto.setOperationType(OperationType.TRANSFER);
        internalOperationDto.setMoney(new BigDecimal(124));

        MoneyTransfer moneyTransfer = new MoneyTransfer();

        MoneyTransferDto moneyTransferDto = new MoneyTransferDto();
        moneyTransferDto.setMoney(new BigDecimal(123));
        moneyTransferDto.setAccountNameTo("to");
        moneyTransferDto.setAccountNameFrom("from");
        moneyTransferDto.setOperationType(OperationType.TRANSFER);

        List<AbstractTransactionDto> expectedDtoList = new ArrayList<>();
        expectedDtoList.add(moneyTransferDto);
        expectedDtoList.add(internalOperationDto);

        when(accountRepository.findAccountEntityByName(accountName))
                .thenReturn(Optional.of(account));
        when(internalOperationRepository.findAllByAccount(account))
                .thenReturn(Collections.singletonList(internalOperation));
        when(internalOperationEntityToDtoMapper.toDto(internalOperation))
                .thenReturn(internalOperationDto);
        when(moneyTransferRepository.findDistinctByFromOrTo(account, account))
                .thenReturn(Collections.singletonList(moneyTransfer));
        when(moneyTransferEntityToDto.toDto(moneyTransfer))
                .thenReturn(moneyTransferDto);

        //WHEN
        ResponseResult responseResult = adminService.getAllTransactionByAccount(accountName);

        //THEN
        assertEquals(ResponseStatus.SUCCESS, responseResult.getStatus());
        assertEquals(ALL_TRANSACTIONS_BY_ACCOUNT_NAME_INFO, responseResult.getMessage());
        assertEquals(expectedDtoList, responseResult.getBody());
    }

    @Test
    void getAllTransactionalByAccountWhenAccountByNameIsNull() {
        //GIVEN
        String accountName = "accountName";

        when(accountRepository.findAccountEntityByName(accountName))
                .thenReturn(Optional.empty());

        //WHEN
        ResponseResult responseResult = adminService.getAllTransactionByAccount(accountName);

        //THEN
        assertEquals(NO_SUCH_ACCOUNT, responseResult.getMessage());
        assertNull(responseResult.getBody());
        assertEquals(ResponseStatus.ERROR, responseResult.getStatus());
    }
}