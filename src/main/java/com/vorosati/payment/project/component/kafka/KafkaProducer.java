package com.vorosati.payment.project.component.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper(); // Initialize Jackson ObjectMapper
    }

    public void sendTransactionNotification(TransactionNotification notification) {
        try {
            // Serialize the object to JSON
            String message = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("transaction_notifications", message);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            e.printStackTrace();
        }
    }
}
