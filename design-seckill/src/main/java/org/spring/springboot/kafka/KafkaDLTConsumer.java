package org.spring.springboot.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaDLTConsumer {

    // 监听死信队列 topic（DLT = Dead Letter Topic）
    @KafkaListener(topics = "seckill_topic.DLT", groupId = "seckill-dlt-group")
    public void handleDeadLetter(String message) {
        log.error("💀 死信消息处理：{}", message);

        // TODO：这里可以落库、通知运维、告警等
    }
}
