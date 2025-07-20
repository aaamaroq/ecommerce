package com.ecommerce.service.notification.product.adapter.listener;

import com.ecommerce.service.notification.product.adapter.dto.validation.ProductRequestValidator;
import com.ecommerce.service.notification.product.adapter.formatter.ProductDetailsFormatter;
import com.ecommerce.service.notification.product.infrastructure.EmailNotifier;
import com.ecommerce.service.notification.product.adapter.dto.ProductRequest;
import com.ecommerce.service.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.service.notification.product.application.ProductService;
import com.ecommerce.service.notification.product.adapter.exception.ProductNotFoundException;
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
public class ProductRequestListener {

    private final ProductService productService;
    private final ProductDetailsFormatter productDetailsFormatter;
    private final EmailNotifier emailNotifier;
    private final ProductRequestValidator productRequestValidator;
    private final MessageSource messageSource;


    @KafkaListener(topics = "${spring.kafka.topic.request-info}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ProductRequest productRequest) {

        if (!productRequestValidator.isValid(productRequest)) {
            log.warn("Invalid ProductRequest received. Skipping processing.");
            return;
        }

        log.info("Received product request: {}", productRequest);
        Locale locale = resolveLocale(productRequest.getLanguage());
        String productResponseFormatted;

        try {
            ProductResponse productResponse = productService.getProductResponseById(productRequest.getProductId());
            log.debug("Fetched product response: {}", productResponse);
            productResponseFormatted = productDetailsFormatter.format(productResponse, locale);
            log.debug("Formatted product details: {}", productResponseFormatted);

        } catch (ProductNotFoundException ex) {
            log.warn("Product not found for ID: {}", productRequest.getProductId(), ex);
            productResponseFormatted = messageSource.getMessage("product.not.found", null, locale);
        }

        try {
            String subject = messageSource.getMessage("email.subject", null, locale);
            emailNotifier.send(productRequest.getEmail(), subject, productResponseFormatted);
            log.info("Sent product details email to: {}", productRequest.getEmail());
        } catch (Exception ex) {
            log.error("Failed to send email to: {}", productRequest.getEmail(), ex);
        }

    }

    private Locale resolveLocale(String language) {
        Set<String> supportedLanguages = Set.of("en", "es", "de");

        if (language != null && supportedLanguages.contains(language)) {
            return Locale.forLanguageTag(language);
        }

        if (language != null) {
            log.warn("Unsupported language '{}'. Falling back to English.", language);
        }

        return Locale.ENGLISH;
    }
}

