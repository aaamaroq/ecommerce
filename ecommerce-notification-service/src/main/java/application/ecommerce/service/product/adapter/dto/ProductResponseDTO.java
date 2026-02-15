package application.ecommerce.service.product.adapter.dto;

import java.math.BigDecimal;

public record ProductResponseDTO(
    String name,
    BigDecimal price,
    Integer quantity,
    String description,
    BigDecimal rating
) {}
