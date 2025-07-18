package com.ecommerce.product.adapter.controller;

import com.ecommerce.product.adapter.dto.ProductRequest;
import com.ecommerce.product.adapter.messaging.KafkaProductPublisher;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    private final KafkaProductPublisher kafkaProductPublisher;

    public ProductController(KafkaProductPublisher kafkaProductPublisher) {
        this.kafkaProductPublisher = kafkaProductPublisher;
    }

    @GetMapping("/getProduct")
    public ResponseEntity<String> getProduct(
            @RequestParam("id") @NotNull(message = "ID cannot be null") long id,
            @RequestParam("name") @NotBlank(message = "Name cannot be blank") String name,
            @RequestParam("email") @Email(message = "Invalid email") String email) {

        try {
            ProductRequest productRequest = new ProductRequest(name, email, id);

            log.info("Product requested: {}", productRequest);

            kafkaProductPublisher.publishProductRequest(productRequest);

            log.info("Message sent to Kafka for product: {}", productRequest);

            return ResponseEntity.ok("Product request sent");
        } catch (Exception e) {
            log.error("Error processing the request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the request");
        }
    }
}
