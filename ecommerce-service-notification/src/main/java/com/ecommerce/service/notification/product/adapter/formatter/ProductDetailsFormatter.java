package com.ecommerce.service.notification.product.adapter.formatter;

import com.ecommerce.service.notification.product.adapter.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProductDetailsFormatter {

    private final MessageSource messageSource;

    public String format(ProductResponse product, Locale locale) {
        return messageSource.getMessage("email.product.details", null, locale) + ":\n" +
                messageSource.getMessage("email.product.name", null, locale) + ": " + product.getName() + "\n" +
                messageSource.getMessage("email.product.price", null, locale) + ": " + product.getPrice() + "$\n" +
                messageSource.getMessage("email.product.description", null, locale) + ": " + product.getDescription() + "\n" +
                messageSource.getMessage("email.product.quantity", null, locale) + ": " + product.getQuantity() + "\n" +
                messageSource.getMessage("email.product.rating", null, locale) + ": " + product.getRating();
    }
}
