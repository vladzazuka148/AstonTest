package com.example.astontest.user.controller;

import com.example.astontest.core.dto.responsedto.ResponseResult;
import com.example.astontest.user.dto.CreateAccountRequestDto;
import com.example.astontest.user.dto.DepositMoneyRequestDto;
import com.example.astontest.user.dto.TransferMoneyRequestDto;
import com.example.astontest.user.dto.WithdrawMoneyRequestDto;
import com.example.astontest.user.facade.AccountFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final AccountFacade accountFacade;

    @PostMapping("/addAccount")
    ResponseResult createBankAccount(@RequestBody CreateAccountRequestDto createAccountRequestDto) {
        return accountFacade.createAccount(createAccountRequestDto);
    }

    @PostMapping("/deposit")
    ResponseResult depositMoney(@RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
        return accountFacade.depositMoney(depositMoneyRequestDto);
    }

    @PostMapping("/withdraw")
    ResponseResult withdrawMoney(@RequestBody WithdrawMoneyRequestDto withdrawMoneyRequestDto) {
        return accountFacade.withdrawMoney(withdrawMoneyRequestDto);
    }

    @PostMapping("/transfer")
    ResponseResult transferMoney(@RequestBody TransferMoneyRequestDto transferMoneyRequestDto) {
        return accountFacade.transferMoney(transferMoneyRequestDto);
    }
}
