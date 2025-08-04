package com.ecommerce.service.product.infrastructure.messaging;

import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductPublisher {

    private final KafkaTemplate<String, ProductKafkaDTO> kafkaTemplate;

    @Value("${spring.kafka.topic.request-info}")
    private String topic;

    public KafkaProductPublisher(KafkaTemplate<String, ProductKafkaDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishProductRequest(ProductKafkaDTO productKafkaDTO) {
        kafkaTemplate.send(topic, productKafkaDTO);
    }
}

