package com.vorosati.payment.project.component.transaction;

import com.vorosati.payment.project.common.PaymentResponseType;
import com.vorosati.payment.project.common.exception.BusinessException;
import com.vorosati.payment.project.component.account.Account;
import com.vorosati.payment.project.component.account.AccountRepository;
import com.vorosati.payment.project.component.kafka.KafkaProducer;
import com.vorosati.payment.project.component.kafka.TransactionNotification;
import com.vorosati.payment.project.component.kafka.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaProducer kafkaProducer;


    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, KafkaProducer kafkaProducer) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public Transaction sendMoney(Long senderId, Long recipientId, Double amount) {
        if (amount <= 0) {
            throw new BusinessException(PaymentResponseType.INVALID_TRANSACTION);
        }

        Account sender = accountRepository.findByIdForUpdate(senderId)
                .orElseThrow(() -> new BusinessException(PaymentResponseType.ACCOUNT_NOT_FOUND, senderId));
        Account recipient = accountRepository.findByIdForUpdate(recipientId)
                .orElseThrow(() -> new BusinessException(PaymentResponseType.ACCOUNT_NOT_FOUND, recipientId));

        // Ensure sender has enough balance
        if (sender.getBalance() < amount) {
            throw new BusinessException(PaymentResponseType.INSUFFICIENT_BALANCE);
        }

        sender.setBalance(sender.getBalance() - amount);
        recipient.setBalance(recipient.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(recipient);
        Transaction transaction = new Transaction(sender, recipient, amount, LocalDateTime.now());
        transactionRepository.save(transaction);

        // Send Kafka notification
        kafkaProducer.sendTransactionNotification(new TransactionNotification(sender.getId(), amount, sender.getBalance(), TransactionType.OUTCOME));
        kafkaProducer.sendTransactionNotification(new TransactionNotification(recipient.getId(), amount, recipient.getBalance(), TransactionType.INCOME));

        return transaction;
    }

    public List<Transaction> getTransactions(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BusinessException(PaymentResponseType.ACCOUNT_NOT_FOUND, accountId));
        return transactionRepository.findBySenderOrRecipient(account, account);
    }
}
