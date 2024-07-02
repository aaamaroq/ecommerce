package com.ecommerce.Common.Model;

public class ProductRequest {

    private String nombre;
    private String email;
    private Long productId;

    public ProductRequest(String nombre, String email, Long productId) {
        this.nombre = nombre;
        this.email = email;
        this.productId = productId;
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
