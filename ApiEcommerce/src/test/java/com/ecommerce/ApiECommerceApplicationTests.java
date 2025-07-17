package com.ecommerce;

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

import com.ecommerce.Common.Model.ProductRequest;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "product-requests" })
public class ApiECommerceApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private KafkaAdmin kafkaAdmin;

    @Autowired
    private KafkaTemplate<String, ProductRequest> kafkaTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
