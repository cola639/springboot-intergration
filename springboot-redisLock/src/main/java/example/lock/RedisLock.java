package example.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

/**
 * RedisLock 类用于通过 Redis 实现分布式锁。
 */
public class RedisLock {
    private static Jedis jedis; // Jedis 客户端对象，用于操作 Redis
    private String lockKey; // 分布式锁的键名
    private static final String LOCK_SUCCESS = "OK"; // 加锁成功时的返回值常量

    public static void main(String[] args) {
        // 初始化 Jedis 实例
        jedis = new Jedis("localhost", 6379);
        try {
            // 创建一个 RedisLock 对象
            RedisLock lock = new RedisLock(jedis, "myLock");

            //  1“加锁”：如果锁（"myLock"）当前没有被其他进程或线程占用，那么这个操作会在Redis中创建一个名为"myLock"的新键，并将其值设置为"locked"。同时，这个键会有一个30秒钟的过期时间（1000 * 30 毫秒）。这就是所谓的“加锁”操作。
            //  2  如果锁已经存在（意味着另一个进程或线程已经拥有了这个锁），则这个操作不会覆盖现有的锁，而是返回false，表示锁没有被成功获取。
            //  3   isLocked变量将存储方法返回的结果，如果获取锁成功则为true，否则为false
            boolean isLocked = lock.lock(1000 * 30);
            System.out.println("Lock acquired: " + isLocked);

            // 做一些工作
            if (isLocked) {
                System.out.println("Performing some work...");
                // 模拟工作执行时间
                Thread.sleep(1000 * 30);

                // 释放锁
                lock.unlock();
                System.out.println("Lock released");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 确保 Jedis 实例在完成后关闭
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 构造器初始化 RedisLock。
     *
     * @param jedis   Jedis 客户端对象
     * @param lockKey 锁的键名
     */
    public RedisLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }

    /**
     * 尝试获取分布式锁。
     *
     * @param expireTime 锁的过期时间，单位毫秒。
     * @return 加锁成功返回 true，否则返回 false。
     */
    public boolean lock(long expireTime) {
        // 设置锁的参数，nx() 是不存在时才设置，px() 是设置过期时间
        SetParams params = new SetParams();
        params.nx().px(expireTime);
        // 尝试设置分布式锁
        String result = jedis.set(lockKey, "locked", params);
        // 返回加锁的结果，如果返回值为 "OK" 则表示加锁成功
        return LOCK_SUCCESS.equals(result);
    }

    /**
     * 释放分布式锁。
     */
    public void unlock() {
        // 删除对应的锁键
        jedis.del(lockKey);
    }
}
