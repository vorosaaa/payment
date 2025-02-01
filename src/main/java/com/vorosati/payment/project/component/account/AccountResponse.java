package com.vorosati.payment.project.component.account;

public record AccountResponse(String name, Double balance) {
    public AccountResponse (Account account) {
        this(account.getName(), account.getBalance());
    }
}
