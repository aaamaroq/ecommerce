package com.ecommerce.Common.Model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductRequest {

    private String nombre;
    private String email;
    private Long productId;

    // Constructor con argumentos para la deserialización
    @JsonCreator
    public ProductRequest(@JsonProperty("nombre") String nombre, 
                          @JsonProperty("email") String email, 
                          @JsonProperty("productId") Long productId) {
        this.nombre = nombre;
        this.email = email;
        this.productId = productId;
    }

    // Constructor sin argumentos requerido por algunos deserializadores
    public ProductRequest() {
        // Dejar vacío o inicializar valores por defecto si es necesario
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
