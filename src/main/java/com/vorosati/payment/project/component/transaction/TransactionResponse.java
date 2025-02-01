package com.vorosati.payment.project.component.transaction;

import com.vorosati.payment.project.component.account.AccountResponse;

public record TransactionResponse(AccountResponse sender, AccountResponse recipient, Double amount, String timestamp) {
    public TransactionResponse (Transaction transaction) {
        this(new AccountResponse(transaction.getSender()), new AccountResponse(transaction.getRecipient()),
                transaction.getAmount(), transaction.getTimestamp().toString());
    }
}
