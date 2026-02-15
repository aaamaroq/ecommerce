package application.ecommerce.service.product.adapter.listener;

import application.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import application.ecommerce.service.product.adapter.dto.validation.GenericValidator;
import application.ecommerce.service.product.adapter.formatter.ProductDetailsFormatter;
import application.ecommerce.service.product.infrastructure.EmailNotifier;
import application.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import application.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import application.ecommerce.service.product.application.ProductService;
import application.ecommerce.service.product.adapter.exception.ProductNotFoundException;
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
    private final GenericValidator<ProductKafkaDTO> productKafkaDTOValidator;
    private final GenericValidator<ProductKafkaCreateDTO> productKafkaCreateDTOValidator;
    private final MessageSource messageSource;



    @KafkaListener(
            topics = "${spring.kafka.topic.request-info}",
            containerFactory = "productKafkaDTOKafkaListenerContainerFactory"
    )
    public void consumeGetProduct(ProductKafkaDTO productKafkaDTO) {

        if (!productKafkaDTOValidator.isValid(productKafkaDTO)) {
            log.warn("Invalid ProductKafkaDTO received. Skipping processing.");
            return;
        }

        log.info("Received product request: {}", productKafkaDTO);
        Locale locale = resolveLocale(productKafkaDTO.language());
        String FormattedMessage;

        try {
            ProductResponseDTO productResponseDTO = productService.getProductResponseById(productKafkaDTO.productId());
            log.debug("Fetched product response: {}", productResponseDTO);
            FormattedMessage = productDetailsFormatter.formatProductFoundMessage(productResponseDTO, locale);
            log.debug("Formatted product details: {}", FormattedMessage);

        } catch (ProductNotFoundException ex) {
            log.warn("Product not found for ID: {}", productKafkaDTO.productId(), ex);
            String notFoundMessage = messageSource.getMessage("product.not.found", null, locale);
            FormattedMessage = productDetailsFormatter.formatFailedMessage(notFoundMessage);
        }

        try {
            String subject = messageSource.getMessage("email.subject.request", null, locale);
            emailNotifier.send(productKafkaDTO.email(), subject, FormattedMessage);
            log.info("Sent product details email to: {}", productKafkaDTO.email());
        } catch (Exception ex) {
            log.error("Failed to send email to: {} {}", productKafkaDTO.email(), ex.getMessage());
        }

    }

    @KafkaListener(
            topics = "${spring.kafka.topic.product-create}",
            containerFactory = "productKafkaCreateDTOKafkaListenerContainerFactory"
    )
    public void consumeCreateProduct(ProductKafkaCreateDTO productKafkaCreateDTO) {

        if (!productKafkaCreateDTOValidator.isValid(productKafkaCreateDTO)) {
            log.warn("Invalid ProductKafkaCreateDTO received. Skipping processing.");
            return;
        }

        log.info("Received product create request: {}", productKafkaCreateDTO);

        ProductKafkaCreateDTO.ProductData productData = productKafkaCreateDTO.getProduct();
        Locale locale = resolveLocale(productKafkaCreateDTO.getLanguage());
        String email = productKafkaCreateDTO.getNotifyEmail();
        String FormattedMessage = "";
        Long idProductSaved;
        String subject;

        try {

            idProductSaved = productService.saveProduct(productData);

            log.info("Product saved successfully: {}", productData.getName());

            subject = messageSource.getMessage("email.product.create", null, locale);
            FormattedMessage = productDetailsFormatter.formatProductAddedMessage(idProductSaved, locale);

        } catch (Exception ex) {
            log.error("Error saving product: {} - {}", productData.name(), ex.getMessage(), ex);

            subject = messageSource.getMessage("email.product.not.create", null, locale);
            String errorMessage = messageSource.getMessage("email.product.internal.error", null, locale);
            FormattedMessage = productDetailsFormatter.formatFailedMessage(errorMessage);

        }

        try {
            emailNotifier.send(email, subject, FormattedMessage);
            log.info("Sent product details email to: {}", email);
        } catch (Exception ex) {
            log.error("Failed to send email to: {} {}", email, ex.getMessage());
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

