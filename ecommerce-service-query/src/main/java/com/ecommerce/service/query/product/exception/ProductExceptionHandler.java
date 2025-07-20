package com.ecommerce.service.query.product.exception;  // or com.ecommerce.exception

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@Slf4j
@RestControllerAdvice(basePackages = "com.ecommerce.product")
public class ProductExceptionHandler {

    private final MessageSource messageSource;

    public ProductExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, Locale locale) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        String errorMessage = messageSource.getMessage("product.request.failed", null, locale);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleValidationException(ConstraintViolationException ex, Locale locale) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        log.warn("Validation errors: {}", errors);

        String response = String.join("\n- ", errors);
        return ResponseEntity.badRequest().body(response);
    }
}
