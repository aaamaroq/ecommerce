package com.ecommerce.ApiECommerce.Controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.Common.Model.ProductRequest;

import org.springframework.web.bind.annotation.RequestMapping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private KafkaTemplate<String, ProductRequest> kafkaTemplate;

    @Value("${spring.kafka.topic.request-info}")
    private String topic;

    @GetMapping("/getProducto")
    public ResponseEntity<String> getProducto(
            @RequestParam("id") @NotNull(message = "El ID no puede estar vacío") long id,
            @RequestParam("nombre") @NotBlank(message = "El nombre no puede estar vacío") String nombre,
            @RequestParam("email") @Email(message = "Email inválido") String email) {

        try {
            // Validar los parámetros aquí antes de crear el objeto ProductRequest
            ProductRequest productRequest = new ProductRequest(nombre, email, id);
            
            // Log para conocer el producto
            logger.info("Producto solicitado: {}", productRequest.toString());

            kafkaTemplate.send(topic, productRequest);
            
            // Log para confirmar envío a Kafka
            logger.info("Mensaje enviado a Kafka para el producto: {}", productRequest.toString());
            
            return ResponseEntity.ok("Solicitud de producto enviada");
        } catch (Exception e) {
            // Manejo de excepciones
            logger.error("Error al procesar la solicitud", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud");
        }
    }
}


