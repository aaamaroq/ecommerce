package com.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.javamail.JavaMailSender;

import com.ecommerce.notification.product.adapter.dto.ProductRequest;

import org.springframework.mail.SimpleMailMessage;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "product-requests" })
public class ConsumerApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private KafkaTemplate<String, ProductRequest> kafkaTemplate;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void testDataSourceExists() {
        assertThat(dataSource).isNotNull();
    }

    @Test
    public void testDatabaseIsUp() {
        // Realizar una consulta simple para verificar que la base de datos está en funcionamiento
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertThat(result).isEqualTo(1);
    }

    @Test
    public void testKafkaAdminExists() {
        assertThat(kafkaAdmin).isNotNull();
    }

    @Test
    public void testKafkaTemplateExists() {
        assertThat(kafkaTemplate).isNotNull();
    }

    @Test
    public void testKafkaTopicExists() throws InterruptedException, ExecutionException {
        Map<String, Object> configs = kafkaAdmin.getConfigurationProperties();
        try (AdminClient adminClient = AdminClient.create(configs)) {
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> names = topics.names().get();
            assertThat(names).contains("product-requests");
        }
    }

    @Test
    public void testSendEmail() {
        String toEmail = "adrianamaroquero@gmail.com";
        String subject = "Testing email functionality";
        String body = "This is a test email.";

        // Enviar el correo electrónico
        try {
            sendTestEmail(toEmail, subject, body);
        } catch (Exception e) {
            assertThat(false).as("Failed to send email: " + e.getMessage()).isTrue();
        }
    }

    private void sendTestEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}
