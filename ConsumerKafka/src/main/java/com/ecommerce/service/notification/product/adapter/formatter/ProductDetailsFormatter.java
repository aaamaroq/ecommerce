package com.ecommerce.service.notification.product.adapter.formatter;

import com.ecommerce.service.notification.product.adapter.dto.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailsFormatter {

    public String format(ProductResponse product) {
        return "Product details:\n" +
                "Name: " + product.getName() + "\n" +
                "Price: " + product.getPrice() + "$\n" +
                "Description: " + product.getDescription() + "\n" +
                "Quantity: " + product.getQuantity() + "\n" +
                "Rating: " + product.getRating();
    }
}
