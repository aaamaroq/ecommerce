package application.ecommerce.service.product.adapter.formatter;

import application.ecommerce.service.product.adapter.dto.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProductDetailsFormatter {

    private final MessageSource messageSource;

    public String formatProductFoundMessage(ProductResponseDTO product, Locale locale) {
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
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.name() + "</td>" +
                "  </tr>" +
                "  <tr>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + priceLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.price() + " $</td>" +
                "  </tr>" +
                "  <tr style='background-color: #f4f4f4;'>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + descriptionLabel + "</th>"
                +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.description() + "</td>" +
                "  </tr>" +
                "  <tr>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + quantityLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.quantity() + "</td>" +
                "  </tr>" +
                "  <tr style='background-color: #f4f4f4;'>" +
                "    <th style='padding: 8px; border: 1px solid #ddd; text-align: left;'>" + ratingLabel + "</th>" +
                "    <td style='padding: 8px; border: 1px solid #ddd;'>" + product.rating() + "</td>" +
                "  </tr>" +
                "</table>" +
                "</body>" +
                "</html>";
    }

    public String formatFailedMessage(String message) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #e22a2a;'>" + message + "</h2>" +
                "</body>" +
                "</html>";
    }

    public String formatProductAddedMessage(Long productId, Locale locale) {
        String message = messageSource.getMessage("email.product.added", new Object[]{productId}, locale);

        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #28a745;'>" + message + "</h2>" +
                "</body>" +
                "</html>";
    }


}
