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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    public static final String CREATE_BANK_ACCOUNT_SUCCESS = "Вы успешно открыли новый счет";
    public static final String OPERATION_SUCCESS = "Операция прошла успешно, остаток: ";

    private final AccountRepository accountRepository;
    private final MoneyTransferRepository moneyTransferRepository;
    private final InternalOperationRepository internalOperationRepository;

    @Override
    @Transactional
    public ResponseResult createAccount(Account entity) {
        entity.setMoney(new BigDecimal(0));
        accountRepository.save(entity);

        return new ResponseResult(CREATE_BANK_ACCOUNT_SUCCESS,
                ResponseStatus.SUCCESS,
                null);
    }

    @Override
    @Transactional
    public ResponseResult depositMoney(BigDecimal requestDtoMoney,
                                       BigDecimal accountEntityMoney,
                                       Account account) {

        account.setMoney(accountEntityMoney.add(requestDtoMoney));
        accountRepository.save(account);

        InternalOperation internalOperation = createInternalOperation(requestDtoMoney,
                account,
                OperationType.DEPOSIT);

        internalOperationRepository.save(internalOperation);

        return new ResponseResult(OPERATION_SUCCESS + account.getMoney(),
                ResponseStatus.SUCCESS,
                null);
    }

    @Override
    @Transactional
    public ResponseResult withdrawMoney(Account account,
                                        BigDecimal accountEntityMoney,
                                        BigDecimal requestDtoMoney) {
        account.setMoney(accountEntityMoney.subtract(requestDtoMoney));
        accountRepository.save(account);

        InternalOperation internalOperation = createInternalOperation(requestDtoMoney,
                account,
                OperationType.WITHDRAW);

        internalOperationRepository.save(internalOperation);

        return new ResponseResult(OPERATION_SUCCESS + account.getMoney(),
                ResponseStatus.SUCCESS,
                null);
    }

    @Override
    @Transactional
    public ResponseResult transferMoney(Account from,
                                        Account to,
                                        BigDecimal fromMoney,
                                        BigDecimal toMoney,
                                        BigDecimal requestDtoMoney) {

        from.setMoney(fromMoney.subtract(requestDtoMoney));
        to.setMoney(toMoney.add(requestDtoMoney));

        accountRepository.save(from);
        accountRepository.save(to);

        MoneyTransfer moneyTransfer = createMoneyTransfer(requestDtoMoney, from, to);

        moneyTransferRepository.save(moneyTransfer);
        return new ResponseResult(OPERATION_SUCCESS + from.getMoney(),
                ResponseStatus.SUCCESS,
                null);
    }

    private static MoneyTransfer createMoneyTransfer(BigDecimal requestDtoMoney, Account from, Account to) {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setMoney(requestDtoMoney);
        moneyTransfer.setOperationType(OperationType.TRANSFER);
        moneyTransfer.setTransactionDataTime(LocalDateTime.now());
        moneyTransfer.setFrom(from);
        moneyTransfer.setTo(to);
        return moneyTransfer;
    }

    private static InternalOperation createInternalOperation(BigDecimal requestDtoMoney,
                                                             Account account,
                                                             OperationType operationType) {
        InternalOperation internalOperation = new InternalOperation();
        internalOperation.setMoney(requestDtoMoney);
        internalOperation.setTransactionDataTime(LocalDateTime.now());
        internalOperation.setOperationType(operationType);
        internalOperation.setAccount(account);
        return internalOperation;
    }
}
