package com.el.eventlogger.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "event-logger-group")
    public void consume(String message) {
        System.out.println("📥 Received message from Kafka: " + message);
    }
}
