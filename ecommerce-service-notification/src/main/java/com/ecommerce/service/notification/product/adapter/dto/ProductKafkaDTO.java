package com.ecommerce.service.notification.product.adapter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class ProductKafkaDTO {

    @JsonProperty("name")
    @NotNull(message = "name must not be null")
    private String name;

    @JsonProperty("email")
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotNull(message = "Product ID must not be null")
    @NotBlank(message = "Product ID must not be blank")
    @JsonProperty("productId")
    private Long productId;

    @NotNull(message = "language must not be null")
    @JsonProperty("language")
    private String language;
}
