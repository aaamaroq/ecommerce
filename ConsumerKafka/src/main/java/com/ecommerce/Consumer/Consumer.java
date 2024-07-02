package com.ecommerce.Consumer;


import com.ecommerce.Common.Model.Product;
import com.ecommerce.Common.Model.ProductRequest;
import com.ecommerce.Common.Service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "${spring.kafka.topic.request-info}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ProductRequest productRequest) {
        logger.info("Received product request: " + productRequest);

        String productDetails = generateProductDetails(productRequest.getProductId());
        sendEmail(productRequest.getEmail(), productDetails);
    }

    public String generateProductDetails(Long productId) {
        Product product = productService.findProductById(productId);

        if (product == null) {
            return "Product not found.";
        }

        return "Product details:\n" +
               "Name: " + product.getName() + "\n" +
               "Price: " + product.getPrice() + "$\n" +
               "Description: " + product.getDescription() + "\n" +
               "Quantity: " + product.getQuantity() + "\n" +
               "Rating: " + product.getRating();
    }

    public void sendEmail(String email, String productDetails) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Details of the requested product");
        mailMessage.setText(productDetails);

        try {
            javaMailSender.send(mailMessage);
            logger.info("Email sent successfully to " + email);
        } catch (Exception e) {
            logger.error("Error sending email to " + email, e);
        }
    }
}
