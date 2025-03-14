package example.lock;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * RedisLockTest 类用于测试 RedisLock 类的功能。
 */
public class RedisLockTest {
    private Jedis jedis; // Jedis 客户端对象
    private RedisLock lock; // RedisLock 实例

    /**
     * 在每个测试之前执行。
     * 建立与 Redis 的连接，并初始化锁。
     */
    @Before
    public void setUp() {
        // 连接到本地的 Redis 服务器
        jedis = new Jedis("localhost", 6379);
        // 实例化 RedisLock 对象
        lock = new RedisLock(jedis, "myLock");
    }

    /**
     * 在每个测试之后执行。
     * 关闭与 Redis 的连接。
     */
    @After
    public void tearDown() {
        // 关闭 Jedis 客户端连接
        jedis.close();
    }

    /**
     * 测试加锁功能。
     * 首次调用加锁应成功，重复加锁应失败。
     */
    @Test
    public void testLock() {
        assertTrue(lock.lock(10000)); // 尝试获得10秒钟的锁 应该成功
        assertFalse(lock.lock(10000)); // 再次尝试获取锁，应该失败
    }

    /**
     * 测试解锁功能。
     * 加锁后解锁，然后再次尝试加锁应该成功。
     */
    @Test
    public void testUnlock() throws InterruptedException {
        // 加锁，断言成功
        assertTrue("加锁失败", lock.lock(10000));

        // 解锁
        lock.unlock();

        // 添加短暂的延时，确保解锁命令被处理
        Thread.sleep(100); // 这里需要处理 InterruptedException

        // 再次尝试加锁，并断言成功
        assertTrue("解锁后再次加锁失败", lock.lock(10000));
    }

}
