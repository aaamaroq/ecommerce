package com.ecommerce.service.notification.product.adapter.dto.validation;

import com.ecommerce.service.notification.product.adapter.dto.ProductKafkaDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRequestValidator {

    private final Validator validator;

    public boolean isValid(ProductKafkaDTO request) {
        Set<ConstraintViolation<ProductKafkaDTO>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            log.warn("Validation failed for ProductRequest:");
            for (ConstraintViolation<ProductKafkaDTO> violation : violations) {
                log.warn(" - {}: {}", violation.getPropertyPath(), violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
