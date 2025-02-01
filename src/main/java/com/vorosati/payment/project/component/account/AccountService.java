package com.vorosati.payment.project.component.account;

import org.springframework.stereotype.Service;

import java.util.Optional;

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

        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }
}
