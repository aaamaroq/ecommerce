package com.ecommerce.service.api.product.exception;  // or com.ecommerce.exception

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ProductExceptionHandler {

    private final MessageSource messageSource;

    public ProductExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleGenericException(Exception ex, Locale locale) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        String errorMessage = messageSource.getMessage("product.request.failed", null, locale);

        Map<String, String> error = new HashMap<>();
        error.put("error", errorMessage);

        return error;
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @RequestMapping(produces = "application/json; charset=UTF-8")
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
