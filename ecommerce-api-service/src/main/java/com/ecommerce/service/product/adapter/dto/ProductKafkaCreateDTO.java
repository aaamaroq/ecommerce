package com.ecommerce.service.product.adapter.dto;

public record ProductKafkaCreateDTO(
    ProductData product,
    String notifyEmail,
    String language
) {
    public record ProductData(
        String name,
        Double price,
        String description,
        Integer quantity,
        Double rating
    ) {}
}
