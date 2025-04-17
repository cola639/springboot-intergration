package org.spring.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.spring.springboot.kafka.SeckillProducer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeckillService {

    private final RedissonClient redissonClient;
    private final StringRedisTemplate redisTemplate;
    private final SeckillProducer seckillProducer;

    public String seckill(Long productId) {
        String lockKey = "seckill:lock:" + productId;
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(5, TimeUnit.SECONDS);
            if (!locked) {
                return "抢购太火爆，请稍后再试";
            }

            String stockStr = redisTemplate.opsForValue().get("seckill:stock:" + productId);
            if (stockStr == null) {
                return "商品未上架";
            }

            int stock = Integer.parseInt(stockStr);
            if (stock <= 0) {
                return "已售罄";
            }

            redisTemplate.opsForValue().decrement("seckill:stock:" + productId);
            seckillProducer.sendSeckillMessage(productId);
            return "秒杀请求已提交 ✅";
        } catch (Exception e) {
            log.error("秒杀异常: {}", e.getMessage(), e);
            return "秒杀失败";
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }
}
