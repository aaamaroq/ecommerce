package com.ecommerce.service.query.product.adapter.controller;

import com.ecommerce.service.query.product.adapter.dto.ProductRequest;
import com.ecommerce.service.query.product.adapter.messaging.KafkaProductPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@Validated
@RestController
@RequestMapping("/product")
@Tag(name = "Product API", description = "API for product operations")
public class ProductController {

    private final KafkaProductPublisher kafkaProductPublisher;
    private final MessageSource messageSource;

    @Autowired
    public ProductController(KafkaProductPublisher kafkaProductPublisher, MessageSource messageSource) {
        this.kafkaProductPublisher = kafkaProductPublisher;
        this.messageSource = messageSource;
    }

    @GetMapping("/getProduct")
    @Operation(
            summary = "Get a product",
            description = "Retrieve a product by ID, name, and email. Sends a Kafka message with the product info."
    )
    public ResponseEntity<String> getProduct(

            @Parameter(description = "Unique identifier of the product", required = true)
            @RequestParam("id") @NotNull(message = "{error.id.required}") Long id,

            @Parameter(description = "Name of the product", required = true)
            @RequestParam("name") @NotBlank(message = "{error.name.required}") String name,

            @Parameter(description = "User email for confirmation", required = true)
            @RequestParam("email") @Email(message = "{error.email.invalid}") String email,

            Locale locale
    ) {

        ProductRequest productRequest = new ProductRequest(name, email, id);
        log.info("Product requested: {}", productRequest);

        kafkaProductPublisher.publishProductRequest(productRequest);
        log.info("Message sent to Kafka for product: {}", productRequest);

        String message = messageSource.getMessage("product.request.sent", null, locale);
        return ResponseEntity.ok(message);
    }
}
