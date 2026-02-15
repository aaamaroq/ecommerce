package com.ecommerce.service.product.adapter.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequestDTO(
    @Parameter(description = "Product information", required = true)
    @Valid
    @NotNull(message = "{error.product.required}")
    ProductData product,

    @Parameter(description = "Email to notify after product creation")
    @Email(message = "{error.email.invalid}")
    @NotBlank(message = "{error.email.required}")
    String notifyEmail
) {
    public record ProductData(
        @Parameter(description = "Name of the product", required = true)
        @NotBlank(message = "{error.name.required}")
        String name,

        @Parameter(description = "Price of the product", required = true, example = "35.50")
        @NotNull(message = "{error.price.required}")
        @DecimalMin(value = "0.0", inclusive = false, message = "{error.price.positive}")
        Double price,

        @Parameter(description = "Description of the product", required = true)
        @NotBlank(message = "{error.description.required}")
        String description,

        @Parameter(description = "Quantity available of the product", required = true, example = "60")
        @NotNull(message = "{error.quantity.required}")
        @Min(value = 0, message = "{error.quantity.min}")
        Integer quantity,

        @Parameter(description = "Rating of the product", required = true, example = "4.20")
        @NotNull(message = "{error.rating.required}")
        @DecimalMin(value = "0.0", inclusive = true, message = "{error.rating.min}")
        Double rating
    ) {}
}
