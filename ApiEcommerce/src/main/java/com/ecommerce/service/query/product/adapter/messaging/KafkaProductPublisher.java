package com.ecommerce.service.query.product.adapter.messaging;

import com.ecommerce.service.query.product.adapter.dto.ProductRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductPublisher {

    private final KafkaTemplate<String, ProductRequest> kafkaTemplate;

    @Value("${spring.kafka.topic.request-info}")
    private String topic;

    public KafkaProductPublisher(KafkaTemplate<String, ProductRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductRequest(ProductRequest productRequest) {
        kafkaTemplate.send(topic, productRequest);
    }
}

