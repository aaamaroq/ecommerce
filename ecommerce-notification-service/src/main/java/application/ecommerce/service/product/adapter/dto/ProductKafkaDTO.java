package application.ecommerce.service.product.adapter.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data; // Provee getters, setters, equals, hashCode, toString

@Data
public class ProductKafkaDTO {

    @NotNull(message = "name must not be null")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotNull(message = "Product ID must not be null")
    private Long productId;

    @NotNull(message = "language must not be null")
    private String language;

    @JsonCreator
    public ProductKafkaDTO(
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("productId") Long productId,
            @JsonProperty("language") String language) {
        this.name = name;
        this.email = email;
        this.productId = productId;
        this.language = language;
    }
}
