package com.ecommerce.service.product.adapter.listener;

import com.ecommerce.service.product.adapter.dto.validation.ProductKafkaDTOValidator;
import com.ecommerce.service.product.adapter.formatter.ProductDetailsFormatter;
import com.ecommerce.service.product.infrastructure.EmailNotifier;
import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import com.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import com.ecommerce.service.product.application.ProductService;
import com.ecommerce.service.product.adapter.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductKafkaListener {

    private final ProductService productService;
    private final ProductDetailsFormatter productDetailsFormatter;
    private final EmailNotifier emailNotifier;
    private final ProductKafkaDTOValidator productKafkaDTOValidator;
    private final MessageSource messageSource;


    @KafkaListener(topics = "${spring.kafka.topic.request-info}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ProductKafkaDTO productKafkaDTO) {

        if (!productKafkaDTOValidator.isValid(productKafkaDTO)) {
            log.warn("Invalid ProductKafkaDTO received. Skipping processing.");
            return;
        }

        log.info("Received product request: {}", productKafkaDTO);
        Locale locale = resolveLocale(productKafkaDTO.getLanguage());
        String productResponseFormatted;

        try {
            ProductResponseDTO productResponseDTO = productService.getProductResponseById(productKafkaDTO.getProductId());
            log.debug("Fetched product response: {}", productResponseDTO);
            productResponseFormatted = productDetailsFormatter.format(productResponseDTO, locale);
            log.debug("Formatted product details: {}", productResponseFormatted);

        } catch (ProductNotFoundException ex) {
            log.warn("Product not found for ID: {}", productKafkaDTO.getProductId(), ex);
            String notFoundMessage = messageSource.getMessage("product.not.found", null, locale);
            productResponseFormatted = productDetailsFormatter.formatProductNotFound(notFoundMessage);
        }

        try {
            String subject = messageSource.getMessage("email.subject", null, locale);
            emailNotifier.send(productKafkaDTO.getEmail(), subject, productResponseFormatted);
            log.info("Sent product details email to: {}", productKafkaDTO.getEmail());
        } catch (Exception ex) {
            log.error("Failed to send email to: {} {}", productKafkaDTO.getEmail(), ex.getMessage());
        }

    }

    private Locale resolveLocale(String language) {
        Set<String> supportedLanguages = Set.of("en", "es", "de");

        if (supportedLanguages.contains(language)) {
            return Locale.forLanguageTag(language);
        }

        log.warn("Unsupported language '{}'. Falling back to English.", language);
        return Locale.ENGLISH;
    }
}

