package com.ecommerce.service.product.adapter.dto;

public record ProductKafkaDTO(
    String name,
    String email,
    Long productId,
    String language
) {}
