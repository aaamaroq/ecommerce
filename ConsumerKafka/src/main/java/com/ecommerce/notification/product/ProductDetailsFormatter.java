package com.ecommerce.notification.product;

import com.ecommerce.notification.product.adapter.dto.ProductResponse;
import com.ecommerce.notification.product.domain.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailsFormatter {

    public String format(ProductResponse product) {
        if (product == null) {
            return "Product not found.";
        }
        return "Product details:\n" +
                "Name: " + product.getName() + "\n" +
                "Price: " + product.getPrice() + "$\n" +
                "Description: " + product.getDescription() + "\n" +
                "Quantity: " + product.getQuantity() + "\n" +
                "Rating: " + product.getRating();
    }
}
