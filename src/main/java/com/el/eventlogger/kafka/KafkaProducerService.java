package com.el.eventlogger.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("✅ Sent message to Kafka: " + message);
    }
}
