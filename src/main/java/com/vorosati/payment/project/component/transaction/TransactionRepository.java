package com.vorosati.payment.project.component.transaction;

import com.vorosati.payment.project.component.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrRecipient(Account sender, Account recipient);
}