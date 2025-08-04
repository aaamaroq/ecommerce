package com.ecommerce.service.config;

import com.ecommerce.service.product.adapter.dto.ProductKafkaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.StringDeserializer;
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

    /**
     * Configures the Kafka Consumer Factory with custom deserialization settings.
     */
    @Bean
    public ConsumerFactory<String, ProductKafkaDTO> consumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();

        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "product-requests");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Key deserializer
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Value deserializer with error handling
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ProductKafkaDTO.class.getName());
        consumerProps.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES,
                "com.ecommerce.service.product.adapter.dto, com.ecommerce.service.product.adapter.dto.*");

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    /**
     * Creates a Kafka listener container factory with custom error handling.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductKafkaDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductKafkaDTO> listenerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        listenerFactory.setConsumerFactory(consumerFactory());

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
                    exception // Pasa el exception para que imprima el stack trace
            );
        };

        // Configure the error handler with no retries and commit recovered offsets
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                recoverer,
                new FixedBackOff(0L, 0L) // No retries
        );

        // Prevent retrying deserialization errors
        errorHandler.addNotRetryableExceptions(SerializationException.class);
        errorHandler.setCommitRecovered(true);

        listenerFactory.setCommonErrorHandler(errorHandler);

        return listenerFactory;
    }

    private String safeToString(Object obj) {
        if (obj == null) return "<null>";
        try {
            return obj.toString();
        } catch (Exception e) {
            return "<error in toString()>";
        }
    }

    private String headersToString(Headers headers) {
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
