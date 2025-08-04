package com.ecommerce.service.product.infrastructure.messaging;

import com.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProductPublisher {

    private final KafkaTemplate<String, ProductKafkaDTO> requestKafkaTemplate;
    private final KafkaTemplate<String, ProductKafkaCreateDTO> createKafkaTemplate;

    @Autowired
    public KafkaProductPublisher(
            @Qualifier("productRequestKafkaTemplate") KafkaTemplate<String, ProductKafkaDTO> productRequestKafkaTemplate,
            @Qualifier("productCreateKafkaTemplate") KafkaTemplate<String, ProductKafkaCreateDTO> productCreateKafkaTemplate
    ) {
        this.requestKafkaTemplate = productRequestKafkaTemplate;
        this.createKafkaTemplate = productCreateKafkaTemplate;
    }


    @Value("${spring.kafka.topic.request-info}")
    private String requestInfoTopic;

    @Value("${spring.kafka.topic.product-create}")
    private String productCreateTopic;

    public void publishProductRequest(ProductKafkaDTO productKafkaDTO) {
        requestKafkaTemplate.send(requestInfoTopic, productKafkaDTO);
    }

    public void publishProductCreate(ProductKafkaCreateDTO productKafkaCreateDTO) {
        createKafkaTemplate.send(productCreateTopic, productKafkaCreateDTO);
    }
}
