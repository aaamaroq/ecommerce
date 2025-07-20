package com.ecommerce.service.notification.product.adapter.listener;

import com.ecommerce.service.notification.product.adapter.formatter.ProductDetailsFormatter;
import com.ecommerce.service.notification.product.infrastructure.EmailNotifier;
import com.ecommerce.service.notification.product.adapter.dto.ProductRequest;
import com.ecommerce.service.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.service.notification.product.application.ProductService;
import com.ecommerce.service.notification.product.adapter.exception.ProductNotFoundException;
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
    public void consume(ProductRequest productRequest) {
        log.info("Received product request: {}", productRequest);

        String productResponseFormatted;

        try{
        ProductResponse productResponse = productService.getProductResponseById(productRequest.getProductId());
        productResponseFormatted = productDetailsFormatter.format(productResponse);
        } catch (ProductNotFoundException ex){
            productResponseFormatted = "Product not found.";
        }

        emailNotifier.send(productRequest.getEmail(), "Details of the requested product", productResponseFormatted);
    }
}
