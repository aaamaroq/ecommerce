package com.ecommerce.service.query.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {

    private String name;
    private String email;
    private Long productId;
}
