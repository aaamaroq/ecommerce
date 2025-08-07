package com.ecommerce.service.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKafkaCreateDTO {

    private ProductData product;
    private String notifyEmail;
    private String language;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductData {
        private String name;
        private Double price;
        private String description;
        private Integer quantity;
        private Double rating;
    }
}
