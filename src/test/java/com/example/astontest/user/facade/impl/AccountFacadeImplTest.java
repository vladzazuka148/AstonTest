package com.example.astontest.user.facade.impl;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.astontest.user.facade.impl.AccountFacadeImpl.BANK_ACCOUNT_EXIST_WITH_NAME;
import static com.example.astontest.user.facade.impl.AccountFacadeImpl.INCORRECT_PINCODE;
import static com.example.astontest.user.facade.impl.AccountFacadeImpl.INVALID_COUNT_OF_CHARS_FOR_PINCODE;
import static com.example.astontest.user.facade.impl.AccountFacadeImpl.NOT_ENOUGHT_MONEY;
import static com.example.astontest.user.facade.impl.AccountFacadeImpl.NO_SUCH_ACCOUNT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountFacadeImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CreateAccountRequestDtoToAccountEntityMapper createAccountRequestDtoToAccountEntityMapper;
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountFacadeImpl accountFacade;

    @Test
    void createAccount() {
        //GIVEN
        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setPincode("1231");
        createAccountRequestDto.setName("name");

        Account account = new Account();

        when(accountRepository.findAccountEntityByName("name")).thenReturn(Optional.empty());
        when(createAccountRequestDtoToAccountEntityMapper.toBankAccountEntity(createAccountRequestDto))
                .thenReturn(account);

        //WHEN
        accountFacade.createAccount(createAccountRequestDto);

        //THEN
        verify(accountService).createAccount(account);
    }

    @Test
    void createAccountWhenAccountExist() {
        //GIVEN
        String expectedName = "name";

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setPincode("1231");
        createAccountRequestDto.setName(expectedName);

        Account account = new Account();

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.of(account));

        //WHEN
        ResponseResult result = accountFacade.createAccount(createAccountRequestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(BANK_ACCOUNT_EXIST_WITH_NAME +
                createAccountRequestDto.getName(), result.getMessage());
        assertEquals(ResponseStatus.ERROR, result.getStatus());

    }

    @Test
    void createAccountWhenPincodeIsInvalid() {
        //GIVEN
        String expectedName = "name";

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setPincode("1231sdf23");
        createAccountRequestDto.setName(expectedName);

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.empty());

        //WHEN
        ResponseResult result = accountFacade.createAccount(createAccountRequestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(INVALID_COUNT_OF_CHARS_FOR_PINCODE, result.getMessage());
        assertEquals(ResponseStatus.ERROR, result.getStatus());

    }

    @Test
    void depositMoney() {
        //GIVEN
        String expectedName = "name";
        BigDecimal expectedDtoMoney = new BigDecimal(1241);
        BigDecimal expectedEntityMoney = new BigDecimal(12125);

        DepositMoneyRequestDto requestDto = new DepositMoneyRequestDto();
        requestDto.setMoney(expectedDtoMoney);
        requestDto.setName(expectedName);

        Account account = new Account();
        account.setMoney(expectedEntityMoney);

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.of(account));

        //WHEN
        accountFacade.depositMoney(requestDto);

        //THEN
        verify(accountService).depositMoney(expectedDtoMoney,
                expectedEntityMoney,
                account);
    }

    @Test
    void depositMoneyWhenAccountIsNull() {
        //GIVEN
        String expectedName = "name";

        DepositMoneyRequestDto requestDto = new DepositMoneyRequestDto();
        requestDto.setMoney(new BigDecimal(1241));
        requestDto.setName(expectedName);

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.empty());

        //WHEN
        ResponseResult result = accountFacade.depositMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(NO_SUCH_ACCOUNT, result.getMessage());
    }

    @Test
    void withdrawMoney() {
        //GIVEN
        String expectedName = "name";
        String expectedPincode = "1251";

        WithdrawMoneyRequestDto requestDto = new WithdrawMoneyRequestDto();
        requestDto.setMoney(new BigDecimal(12));
        requestDto.setName(expectedName);
        requestDto.setPincode(expectedPincode);

        Account account = new Account();
        account.setPincode(expectedPincode);
        account.setMoney(new BigDecimal(1678));

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.of(account));

        //WHEN
        accountFacade.withdrawMoney(requestDto);

        //THEN
        verify(accountService).withdrawMoney(account,
                account.getMoney(),
                requestDto.getMoney());
    }

    @Test
    void withdrawMoneyWhenNotEnoughMoney() {
        //GIVEN
        String expectedName = "name";
        String expectedPincode = "1251";

        WithdrawMoneyRequestDto requestDto = new WithdrawMoneyRequestDto();
        requestDto.setMoney(new BigDecimal(12512));
        requestDto.setName(expectedName);
        requestDto.setPincode(expectedPincode);

        Account account = new Account();
        account.setPincode(expectedPincode);
        account.setMoney(new BigDecimal(12));

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.of(account));

        //WHEN
        ResponseResult result = accountFacade.withdrawMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(NOT_ENOUGHT_MONEY, result.getMessage());
    }

    @Test
    void withdrawMoneyWhenAccountIsNull() {
        //GIVEN
        String expectedName = "name";
        String expectedPincode = "1251";

        WithdrawMoneyRequestDto requestDto = new WithdrawMoneyRequestDto();
        requestDto.setMoney(new BigDecimal(12512));
        requestDto.setName(expectedName);
        requestDto.setPincode(expectedPincode);

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.empty());

        //WHEN
        ResponseResult result = accountFacade.withdrawMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(NO_SUCH_ACCOUNT, result.getMessage());
    }

    @Test
    void withdrawMoneyWhenPincodeIsInvalid() {
        //GIVEN
        String expectedName = "name";
        String expectedPincode = "1252131";

        WithdrawMoneyRequestDto requestDto = new WithdrawMoneyRequestDto();
        requestDto.setMoney(new BigDecimal(12512));
        requestDto.setName(expectedName);
        requestDto.setPincode(expectedPincode);

        Account account = new Account();
        account.setPincode("1242");

        when(accountRepository.findAccountEntityByName(expectedName))
                .thenReturn(Optional.of(account));

        //WHEN
        ResponseResult result = accountFacade.withdrawMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(INCORRECT_PINCODE, result.getMessage());
    }

    @Test
    void transferMoney() {
        //GIVEN
        BigDecimal expectedMoney = new BigDecimal(41234);
        String expectedPincode = "1235";
        String accountNameTo = "1";
        String accountNameFrom = "2";

        TransferMoneyRequestDto requestDto = new TransferMoneyRequestDto();
        requestDto.setMoney(expectedMoney);
        requestDto.setPincode(expectedPincode);
        requestDto.setAccountNameTo(accountNameTo);
        requestDto.setAccountNameFrom(accountNameFrom);

        Account accountfrom = new Account();
        accountfrom.setMoney(new BigDecimal(100000));
        accountfrom.setPincode("1235");
        Account accountTo = new Account();
        accountTo.setMoney(new BigDecimal(12));

        when(accountRepository.findAccountEntityByName(accountNameFrom))
                .thenReturn(Optional.of(accountfrom));
        when(accountRepository.findAccountEntityByName(accountNameTo))
                .thenReturn(Optional.of(accountTo));

        //WHEN
        accountFacade.transferMoney(requestDto);

        //THEN
        verify(accountService).transferMoney(accountfrom,
                accountTo,
                accountfrom.getMoney(),
                accountTo.getMoney(),
                requestDto.getMoney());
    }

    @Test
    void transferMoneyWhenNotEnoughMoney() {
        //GIVEN
        BigDecimal expectedMoney = new BigDecimal(41234);
        String expectedPincode = "1235";
        String accountNameTo = "1";
        String accountNameFrom = "2";

        TransferMoneyRequestDto requestDto = new TransferMoneyRequestDto();
        requestDto.setMoney(expectedMoney);
        requestDto.setPincode(expectedPincode);
        requestDto.setAccountNameTo(accountNameTo);
        requestDto.setAccountNameFrom(accountNameFrom);

        Account accountfrom = new Account();
        accountfrom.setMoney(new BigDecimal(1));
        accountfrom.setPincode("1235");
        Account accountTo = new Account();

        when(accountRepository.findAccountEntityByName(accountNameFrom))
                .thenReturn(Optional.of(accountfrom));
        when(accountRepository.findAccountEntityByName(accountNameTo))
                .thenReturn(Optional.of(accountTo));

        //WHEN
        ResponseResult result = accountFacade.transferMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(NOT_ENOUGHT_MONEY, result.getMessage());
    }

    @Test
    void transferMoneyWhenPincodeIsInvalid() {
        //GIVEN
        BigDecimal expectedMoney = new BigDecimal(41234);
        String expectedPincode = "1235";
        String accountNameTo = "1";
        String accountNameFrom = "2";

        TransferMoneyRequestDto requestDto = new TransferMoneyRequestDto();
        requestDto.setMoney(expectedMoney);
        requestDto.setPincode(expectedPincode);
        requestDto.setAccountNameTo(accountNameTo);
        requestDto.setAccountNameFrom(accountNameFrom);

        Account accountfrom = new Account();
        accountfrom.setPincode("12");
        Account accountTo = new Account();

        when(accountRepository.findAccountEntityByName(accountNameFrom))
                .thenReturn(Optional.of(accountfrom));
        when(accountRepository.findAccountEntityByName(accountNameTo))
                .thenReturn(Optional.of(accountTo));

        //WHEN
        ResponseResult result = accountFacade.transferMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(INCORRECT_PINCODE, result.getMessage());
    }

    @Test
    void transferMoneyWhenAccountsIsNull() {
        //GIVEN
        BigDecimal expectedMoney = new BigDecimal(41234);
        String expectedPincode = "1235";
        String accountNameTo = "1";
        String accountNameFrom = "2";

        TransferMoneyRequestDto requestDto = new TransferMoneyRequestDto();
        requestDto.setMoney(expectedMoney);
        requestDto.setPincode(expectedPincode);
        requestDto.setAccountNameTo(accountNameTo);
        requestDto.setAccountNameFrom(accountNameFrom);

        when(accountRepository.findAccountEntityByName(accountNameFrom))
                .thenReturn(Optional.empty());
        when(accountRepository.findAccountEntityByName(accountNameTo))
                .thenReturn(Optional.empty());

        //WHEN
        ResponseResult result = accountFacade.transferMoney(requestDto);

        //THEN
        assertNull(result.getBody());
        assertEquals(ResponseStatus.ERROR, result.getStatus());
        assertEquals(NO_SUCH_ACCOUNT, result.getMessage());
    }
}