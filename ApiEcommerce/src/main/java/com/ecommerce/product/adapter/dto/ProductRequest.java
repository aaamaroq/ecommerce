package com.ecommerce.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {

    private String nombre;
    private String email;
    private Long productId;
}
