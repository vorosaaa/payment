package com.vorosati.payment.project.component.kafka;

public record TransactionNotification (Long userId, Double amount, Double balance, TransactionType type) {
}
