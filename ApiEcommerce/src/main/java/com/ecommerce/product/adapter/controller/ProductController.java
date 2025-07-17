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

    @GetMapping("/getProducto")
    public ResponseEntity<String> getProducto(
            @RequestParam("id") @NotNull(message = "El ID no puede estar vacío") long id,
            @RequestParam("nombre") @NotBlank(message = "El nombre no puede estar vacío") String nombre,
            @RequestParam("email") @Email(message = "Email inválido") String email) {

        try {
            ProductRequest productRequest = new ProductRequest(nombre, email, id);

            log.info("Producto solicitado: {}", productRequest);

            kafkaProductPublisher.publishProductRequest(productRequest);

            log.info("Mensaje enviado a Kafka para el producto: {}", productRequest);

            return ResponseEntity.ok("Solicitud de producto enviada");
        } catch (Exception e) {
            log.error("Error al procesar la solicitud", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }
}
