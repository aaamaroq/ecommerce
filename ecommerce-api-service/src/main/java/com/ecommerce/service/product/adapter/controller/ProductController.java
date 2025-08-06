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
                productRequestDTO.getName(),
                productRequestDTO.getEmail(),
                productRequestDTO.getId(),
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
                        productCreateRequestDTO.getProduct().getName(),
                        productCreateRequestDTO.getProduct().getPrice(),
                        productCreateRequestDTO.getProduct().getDescription(),
                        productCreateRequestDTO.getProduct().getQuantity(),
                        productCreateRequestDTO.getProduct().getRating()
                ),
                productCreateRequestDTO.getNotifyEmail(),
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
