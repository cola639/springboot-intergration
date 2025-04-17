package org.spring.springboot.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeckillConsumer {

    private final ProductRepository productRepository;

    @KafkaListener(topics = "seckill_topic", groupId = "seckill-group")
    public void consume(String productIdStr) {
        Long productId = Long.valueOf(productIdStr);
        productRepository.findById(productId).ifPresent(product -> {
            product.setStock(product.getStock() - 1);
            productRepository.save(product);
            log.info("Kafka消费成功，已扣减库存，商品ID: {}", productId);
        });
    }



    @KafkaListener(topics = "seckill_topic", groupId = "seckill-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeDeadLine(String message) {
        log.info("🎯 收到 Kafka 消息: {}", message);

        Long productId = Long.valueOf(message);

        // 模拟异常重试测试
        if ("999".equals(message)) {
            throw new RuntimeException("模拟异常");
        }

        productRepository.findById(productId).ifPresent(product -> {
            product.setStock(product.getStock() - 1);
            productRepository.save(product);
            log.info("✅ 成功消费并更新库存：{}", productId);
        });
    }

}
