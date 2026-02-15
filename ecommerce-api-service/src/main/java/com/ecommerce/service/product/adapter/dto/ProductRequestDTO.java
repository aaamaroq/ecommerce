package com.ecommerce.service.product.adapter.dto;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
    @Parameter(description = "Unique identifier of the product", required = true)
    @NotNull(message = "{error.id.required}")
    Long id,

    @Parameter(description = "Name of the product", required = true)
    @NotBlank(message = "{error.name.required}")
    String name,

    @Parameter(description = "User email for confirmation", required = true)
    @NotBlank(message = "{error.email.required}")
    @Email(message = "{error.email.invalid}")
    String email
) {}
