package org.spring.springboot.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.backoff.FixedBackOff;

import java.util.function.BiConsumer;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaTemplate<Object, Object> kafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        // 🔁 消费失败后转发到 DLT Topic（如 seckill_topic.DLT）
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        // ⏳ 每次重试间隔 3 秒，最多重试 2 次（共尝试 3 次）
        FixedBackOff fixedBackOff = new FixedBackOff(3000L, 2);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, fixedBackOff);

        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }
}
