package com.springMovie.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisStartupChecker implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        try {
            // 设置 Key，过期时间 10 秒
            redisTemplate.opsForValue().set("redis_test_key", "success", 10, TimeUnit.SECONDS);

            // 读取 Redis
            String value = redisTemplate.opsForValue().get("redis_test_key");

            if ("success".equals(value)) {
                System.out.println("✅✅✅ Redis 连接成功！可正常存取数据。（10 秒后自动删除）");
            } else {
                System.out.println("⚠️⚠️⚠️ Redis 连接异常：无法存取数据！");
            }
        } catch (Exception e) {
            System.err.println("❌❌❌ Redis 连接失败！错误信息：" + e.getMessage());
        }
    }
}
