package com.ecommerce.service.notification.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private String description;
    private BigDecimal rating;
}
