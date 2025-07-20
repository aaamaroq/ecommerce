package com.ecommerce.notification.product.adapter.listener;

import com.ecommerce.notification.product.adapter.formatter.ProductDetailsFormatter;
import com.ecommerce.notification.product.adapter.EmailNotifier;
import com.ecommerce.notification.product.adapter.dto.ProductRequest;
import com.ecommerce.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.notification.product.application.ProductService;
import com.ecommerce.notification.product.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductRequestListener {

    private final ProductService productService;
    private final ProductDetailsFormatter productDetailsFormatter;
    private final EmailNotifier emailNotifier;

    public ProductRequestListener(ProductService productService,
                                  ProductDetailsFormatter productDetailsFormatter,
                                  EmailNotifier emailNotifier) {
        this.productService = productService;
        this.productDetailsFormatter = productDetailsFormatter;
        this.emailNotifier = emailNotifier;
    }

    @KafkaListener(topics = "${spring.kafka.topic.request-info}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ProductRequest productRequest) throws ProductNotFoundException {
        log.info("Received product request: {}", productRequest);

        ProductResponse productResponse = productService.getProductResponseById(productRequest.getProductId());
        String productResponseFormatted = productDetailsFormatter.format(productResponse);
        emailNotifier.send(productRequest.getEmail(), "Details of the requested product", productResponseFormatted);
    }
}
