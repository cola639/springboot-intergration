package org.spring.springboot.schedule;

import lombok.extern.slf4j.Slf4j;
import org.spring.springboot.repository.ProductRepository;
import org.spring.springboot.utils.RedisHealthChecker;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HotProductCacheJob {

    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;
    private final RedisHealthChecker redisHealthChecker;

    public HotProductCacheJob(ProductRepository productRepository, StringRedisTemplate redisTemplate, RedisHealthChecker redisHealthChecker) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
        this.redisHealthChecker = redisHealthChecker;
    }


    @Scheduled(fixedRate = 30000)
    public void cacheHotProductStock() {
        if (!redisHealthChecker.isAvailable()) {
            log.warn("⚠️ Redis 不可用，跳过缓存任务");
            return;
        }

        log.info("🔥 定时任务执行中...");
        long productId = 1L;
        String redisKey = "seckill:stock:" + productId;

        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(hasKey)) {
            log.debug("商品 [{}] 已存在于 Redis 缓存中，跳过缓存", productId);
            return;
        }

        productRepository.findById(productId).ifPresent(product -> {
            Integer stock = product.getStock();
            redisTemplate.opsForValue().set(redisKey, stock.toString());
            log.info("商品 [{}] 库存 [{}] 已写入 Redis 缓存 ✅", productId, stock);
        });
    }
}

