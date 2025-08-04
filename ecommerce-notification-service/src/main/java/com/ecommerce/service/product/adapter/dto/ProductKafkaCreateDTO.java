package com.ecommerce.service.product.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductKafkaCreateDTO {

    private String name;
    private Double price;
    private String description;
    private Integer quantity;
    private Double rating;

}
