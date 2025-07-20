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
        String title = messageSource.getMessage("email.product.details", null, locale);
        String nameLabel = messageSource.getMessage("email.product.name", null, locale);
        String priceLabel = messageSource.getMessage("email.product.price", null, locale);
        String descriptionLabel = messageSource.getMessage("email.product.description", null, locale);
        String quantityLabel = messageSource.getMessage("email.product.quantity", null, locale);
        String ratingLabel = messageSource.getMessage("email.product.rating", null, locale);

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #2a7ae2;'>" + title + "</h2>" +
                "<table style='border-collapse: collapse; width: 100%; max-width: 600px;'>" +
                "  <tr style='background-color: #f4f4f4;'>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + nameLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.getName() + "</td>" +
                "  </tr>" +
                "  <tr>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + priceLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.getPrice() + " $</td>" +
                "  </tr>" +
                "  <tr style='background-color: #f4f4f4;'>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + descriptionLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.getDescription() + "</td>" +
                "  </tr>" +
                "  <tr>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + quantityLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.getQuantity() + "</td>" +
                "  </tr>" +
                "  <tr style='background-color: #f4f4f4;'>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + ratingLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.getRating() + "</td>" +
                "  </tr>" +
                "</table>" +
                "</body>" +
                "</html>";
    }
}
