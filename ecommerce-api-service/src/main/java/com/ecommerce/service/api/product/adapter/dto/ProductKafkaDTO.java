package com.ecommerce.service.api.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductKafkaDTO {

    private String name;
    private String email;
    private Long productId;
    private String language;
}
