package com.ecommerce.service.config;

import com.ecommerce.service.product.adapter.dto.ProductKafkaCreateDTO;
import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    // ConsumerFactory para ProductKafkaDTO (topic: product-requests)
    @Bean
    public ConsumerFactory<String, ProductKafkaDTO> productKafkaDTOConsumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "product-requests");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ProductKafkaDTO.class.getName());
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES,
                "com.ecommerce.service.product.adapter.dto, com.ecommerce.service.product.adapter.dto.*");

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    // ConsumerFactory para ProductKafkaCreateDTO (topic: product-creates)
    @Bean
    public ConsumerFactory<String, ProductKafkaCreateDTO> productKafkaCreateDTOConsumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "product-requests"); // Puede ser otro grupo si quieres
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ProductKafkaCreateDTO.class.getName());
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES,
                "com.ecommerce.service.product.adapter.dto, com.ecommerce.service.product.adapter.dto.*");

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    // KafkaListenerContainerFactory para ProductKafkaDTO
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductKafkaDTO> productKafkaDTOKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductKafkaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productKafkaDTOConsumerFactory());
        factory.setCommonErrorHandler(createErrorHandler());
        return factory;
    }

    // KafkaListenerContainerFactory para ProductKafkaCreateDTO
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductKafkaCreateDTO> productKafkaCreateDTOKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductKafkaCreateDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(productKafkaCreateDTOConsumerFactory());
        factory.setCommonErrorHandler(createErrorHandler());
        return factory;
    }

    // Error handler común para ambos listeners
    private DefaultErrorHandler createErrorHandler() {
        ConsumerRecordRecoverer recoverer = (record, exception) -> {
            logger.error(
                    """
                    ❌ Deserialization Error:
                    ├─ Topic       : {}
                    ├─ Partition   : {}
                    ├─ Offset      : {}
                    ├─ Timestamp   : {}
                    ├─ Key         : {}
                    ├─ Raw Value   : {}
                    ├─ Headers     : {}
                    ├─ Exception   : {}: {}
                    """,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.timestamp(),
                    safeToString(record.key()),
                    safeToString(record.value()),
                    headersToString(record.headers()),
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    exception
            );
        };

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(0L, 0L) // No retries
        );

        errorHandler.addNotRetryableExceptions(SerializationException.class);
        errorHandler.setCommitRecovered(true);

        return errorHandler;
    }

    private String safeToString(Object obj) {
        if (obj == null) return "<null>";
        try {
            return obj.toString();
        } catch (Exception e) {
            return "<error in toString()>";
        }
    }

    private String headersToString(org.apache.kafka.common.header.Headers headers) {
        StringBuilder sb = new StringBuilder();
        headers.forEach(header -> {
            sb.append(header.key())
                    .append("=")
                    .append(new String(header.value() != null ? header.value() : new byte[0]))
                    .append("; ");
        });
        return sb.toString();
    }
}
