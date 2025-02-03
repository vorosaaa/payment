package com.vorosati.payment.project;

import com.vorosati.payment.project.common.PaymentResponseType;
import com.vorosati.payment.project.common.exception.BusinessException;
import com.vorosati.payment.project.component.account.Account;
import com.vorosati.payment.project.component.account.AccountRepository;
import com.vorosati.payment.project.component.kafka.KafkaProducer;
import com.vorosati.payment.project.component.kafka.TransactionNotification;
import com.vorosati.payment.project.component.kafka.TransactionType;
import com.vorosati.payment.project.component.transaction.Transaction;
import com.vorosati.payment.project.component.transaction.TransactionRepository;
import com.vorosati.payment.project.component.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendMoney_success() {
        Long senderId = 1L;
        Long recipientId = 2L;
        Double amount = 100.0;

        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(200.0);

        Account recipient = new Account();
        recipient.setId(recipientId);
        recipient.setBalance(50.0);

        when(accountRepository.findByIdForUpdate(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByIdForUpdate(recipientId)).thenReturn(Optional.of(recipient));

        Transaction transaction = transactionService.sendMoney(senderId, recipientId, amount);

        assertNotNull(transaction);
        assertEquals(senderId, transaction.getSender().getId());
        assertEquals(recipientId, transaction.getRecipient().getId());
        assertEquals(amount, transaction.getAmount());

        verify(accountRepository).save(sender);
        verify(accountRepository).save(recipient);
        verify(transactionRepository).save(any(Transaction.class));

        ArgumentCaptor<TransactionNotification> captor = ArgumentCaptor.forClass(TransactionNotification.class);
        verify(kafkaProducer, times(2)).sendTransactionNotification(captor.capture());

        List<TransactionNotification> notifications = captor.getAllValues();
        assertEquals(2, notifications.size());
        assertEquals(TransactionType.OUTCOME, notifications.get(0).type());
        assertEquals(TransactionType.INCOME, notifications.get(1).type());
    }

    @Test
    void sendMoney_insufficientBalance() {
        Long senderId = 1L;
        Long recipientId = 2L;
        Double amount = 100.0;

        Account sender = new Account();
        sender.setId(senderId);
        sender.setBalance(50.0);

        when(accountRepository.findByIdForUpdate(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findByIdForUpdate(recipientId)).thenReturn(Optional.of(sender));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.sendMoney(senderId, recipientId, amount);
        });

        assertEquals(PaymentResponseType.INSUFFICIENT_BALANCE, exception.getResponseType());
    }

    @Test
    void getTransactions_success() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        Transaction transaction1 = new Transaction(account, new Account(), 100.0, LocalDateTime.now());
        Transaction transaction2 = new Transaction(new Account(), account, 50.0, LocalDateTime.now());

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findBySenderOrRecipient(account, account)).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getTransactions(accountId);

        assertNotNull(transactions);
        assertEquals(2, transactions.size());
        assertTrue(transactions.contains(transaction1));
        assertTrue(transactions.contains(transaction2));
    }

    @Test
    void getTransactions_accountNotFound() {
        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            transactionService.getTransactions(accountId);
        });

        assertEquals(PaymentResponseType.ACCOUNT_NOT_FOUND, exception.getResponseType());
    }
}