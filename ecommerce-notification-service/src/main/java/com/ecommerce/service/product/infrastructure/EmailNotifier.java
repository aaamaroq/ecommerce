package com.ecommerce.service.product.infrastructure;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotifier  {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotifier.class);

    private final JavaMailSender javaMailSender;

    public EmailNotifier(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void send(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Error sending email to {}", to, e);
        }
    }
}
