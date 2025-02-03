package com.vorosati.payment.project.component.account;

import com.vorosati.payment.project.common.PaymentResponseType;
import com.vorosati.payment.project.common.exception.BusinessException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountRequest accountRequest) {
        String name = accountRequest.name();
        Double balance = accountRequest.balance();

        Account account = new Account(name, balance != null ? balance : 0.0);
        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new BusinessException(PaymentResponseType.ACCOUNT_NOT_FOUND, accountId));
    }
}
