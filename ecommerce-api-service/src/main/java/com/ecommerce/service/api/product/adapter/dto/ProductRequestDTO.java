package com.ecommerce.service.api.product.adapter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
public class ProductRequestDTO {

    @Parameter(description = "Unique identifier of the product", required = true)
    @NotNull(message = "{error.id.required}")
    private Long id;

    @Parameter(description = "Name of the product", required = true)
    @NotBlank(message = "{error.name.required}")
    private String name;

    @Parameter(description = "User email for confirmation", required = true)
    @NotBlank(message = "{error.email.required}")
    @Email(message = "{error.email.invalid}")
    private String email;

}



