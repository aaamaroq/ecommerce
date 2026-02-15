package com.ecommerce.service.product.adapter.controller;

import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import com.ecommerce.service.product.adapter.dto.ProductCreateRequestDTO;
import com.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import com.ecommerce.service.product.adapter.dto.ProductRequestDTO;
import com.ecommerce.service.product.infrastructure.messaging.KafkaProductPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Controller for handling product-related API requests.
 * Modernized to use Java Records for DTOs and optimized for Virtual Threads.
 */
@Slf4j
@RestController
@RequestMapping("/service/query")
@Tag(name = "Product API", description = "API for product operations")
public class ProductController {

    private final KafkaProductPublisher kafkaProductPublisher;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(KafkaProductPublisher kafkaProductPublisher, MessageSource messageSource) {
        this.kafkaProductPublisher = kafkaProductPublisher;
        this.messageSource = messageSource;
    }

    /**
     * Retrieves product information and publishes a request message to Kafka.
     * 
     * @param productRequestDTO the request details
     * @param locale the current locale for internationalized messages
     * @return a response entity confirming the request was sent
     */
    @GetMapping("/product")
    @Operation(
            summary = "Get a product",
            description = "Retrieve a product by ID, name, and email. Sends a Kafka message with the product info."
    )
    public ResponseEntity<Map<String, String>> getProduct(
            @Valid @ModelAttribute ProductRequestDTO productRequestDTO,
            Locale locale
    ) {

        ProductKafkaDTO productKafkaDTO = new ProductKafkaDTO(
                productRequestDTO.name(),
                productRequestDTO.email(),
                productRequestDTO.id(),
                locale.toLanguageTag()
        );

        log.info("Product requested: {}", productRequestDTO);

        kafkaProductPublisher.publishProductRequest(productKafkaDTO);
        log.info("Message sent to Kafka for product: {}", productKafkaDTO);

        String message = messageSource.getMessage("product.request.sent", null, locale);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Accepts a product creation request and publishes it to Kafka.
     * 
     * @param productCreateRequestDTO the product data and notification email
     * @param locale the current locale for internationalized messages
     * @return a response entity confirming the creation request was sent
     */
    @PostMapping("/product")
    @Operation(
            summary = "Add a product",
            description = "Add a new product and send a Kafka message to the create product topic."
    )
    public ResponseEntity<Map<String, String>> addProduct(
            @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
            Locale locale
    ) {


        log.info("Product requested for create: {}", productCreateRequestDTO);

        ProductKafkaCreateDTO productKafkaCreateDTO = new ProductKafkaCreateDTO(
                new ProductKafkaCreateDTO.ProductData(
                        productCreateRequestDTO.product().name(),
                        productCreateRequestDTO.product().price(),
                        productCreateRequestDTO.product().description(),
                        productCreateRequestDTO.product().quantity(),
                        productCreateRequestDTO.product().rating()
                ),
                productCreateRequestDTO.notifyEmail(),
                locale.toLanguageTag()
        );


        kafkaProductPublisher.publishProductCreate(productKafkaCreateDTO);
        log.info("Message sent to Kafka for create product: {}", productKafkaCreateDTO);

        String message = messageSource.getMessage("product.create.sent", null, locale);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
