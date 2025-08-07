package application.ecommerce.service.product.adapter.dto.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericValidator<T> {

    private final Validator validator;

    public boolean isValid(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            log.warn("Validation failed for {}:", dto.getClass().getSimpleName());
            for (ConstraintViolation<T> violation : violations) {
                log.warn(" - {}: {}", violation.getPropertyPath(), violation.getMessage());
            }
            return false;
        }

        return true;
    }
}
