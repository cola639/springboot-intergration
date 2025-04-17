package org.spring.springboot.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeckillProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendSeckillMessage(Long productId) {
        kafkaTemplate.send("seckill_topic", productId.toString());
        log.info("已发送Kafka消息：{}", productId);
    }
}

