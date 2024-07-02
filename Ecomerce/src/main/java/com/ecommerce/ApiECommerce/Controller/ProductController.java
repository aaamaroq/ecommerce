package com.ecommerce.ApiECommerce.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ApiECommerce.Model.Product;
import com.ecommerce.ApiECommerce.Model.ProductRequest;
import com.ecommerce.ApiECommerce.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, ProductRequest> kafkaTemplate;

    @Value("${spring.kafka.topic.request-info}")
    private String topic;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProducto(
            @PathVariable("id") Long id,
            @RequestParam String nombre,
            @RequestParam String email) {
        Product product = productService.findProductoById(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Enviar solicitud a Kafka
        ProductRequest request = new ProductRequest(nombre, email, id);
        kafkaTemplate.send(topic, request);

        return ResponseEntity.ok(product);
    }
}
