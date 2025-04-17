//package org.spring.springboot.config;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.listener.DefaultErrorHandler;
//import org.springframework.util.backoff.FixedBackOff;
//
//import java.util.function.BiConsumer;
//
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
//            ConsumerFactory<String, String> consumerFactory) {
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//
//        factory.setConsumerFactory(consumerFactory);
//
//        // ✅ 设置重试机制：每次间隔 3 秒，最多重试 3 次（总共处理 4 次）
//        FixedBackOff fixedBackOff = new FixedBackOff(3000L, 3L);
//
//        // ✅ 设置异常处理器
//        BiConsumer<ConsumerRecord<?, ?>, Exception> errorHandler = (record, exception) -> {
//            System.err.println("❌ 消费失败: " + record.value() + ", 错误信息: " + exception.getMessage());
//        };
//
//        factory.setCommonErrorHandler(new DefaultErrorHandler(errorHandler, fixedBackOff));
//
//        return factory;
//    }
//}
