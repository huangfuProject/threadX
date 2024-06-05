package com.threadx.metrics.server.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redis实现的分布式锁
 *
 * @author huangfu
 */
public class RedisDistributedLockTemplate implements DistributedLockTemplate {
    private static final long serialVersionUID = -4317539052451845335L;
    /**
     * redis 客户端操作
     */
    private final RedissonClient redissonClient;

    public RedisDistributedLockTemplate(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }


    /**
     * 获取锁（如果有）并立即返回true值。 如果锁不可用，则此方法将立即返回false值。
     *
     * @param lockName 锁名称
     * @return 是否获取锁成功
     */
    @Override
    public boolean tryLock(String lockName) {
        //构建锁对象
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock();
    }


    /**
     * 尝试获取具有定义的leaseTime的锁。 如有必要，等待定义的maxWaitTime直到锁可用。 在定义的leaseTime间隔后，锁将自动释放。
     *
     * @param lockName    锁名称
     * @param maxWaitTime 最大的等待时间 单位（秒）
     * @param leaseTime   锁超时时间
     * @param timeUnit    时间单位
     * @return 是否获取锁成功
     * @throws InterruptedException 睡眠中断
     */
    @Override
    public boolean tryLock(String lockName, long maxWaitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
        //构建锁对象
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock(maxWaitTime, leaseTime, timeUnit);
    }


    /**
     * 尝试获取具有定义的leaseTime的锁。 如有必要，等待定义的maxWaitTime直到锁可用。
     * 该锁没有超时时间
     *
     * 该锁会进行自动续期 且为可重入锁
     * 看门狗机制
     * 看门口的自动续约事件为 30秒（默认）也就是说 无续约的情况下会30秒锁超时
     * 每隔10s 续期一次
     *
     * @param lockName    锁名称
     * @param maxWaitTime 最大等待时间
     * @param timeUnit 最大等待时间单位
     * @return 是否加锁成功
     * @throws InterruptedException 中断错误
     */
    @Override
    public boolean tryLock(String lockName, long maxWaitTime, TimeUnit timeUnit) throws InterruptedException {
        //构建锁对象
        RLock lock = redissonClient.getLock(lockName);
        return lock.tryLock(timeUnit.toMillis(maxWaitTime), -1, TimeUnit.SECONDS);
    }

    /**
     * 加锁 基于锁名称
     * 不得到锁不罢休
     *
     * @param lockName 锁名称
     */
    @Override
    public void lock(String lockName) {
        RLock fairLock = redissonClient.getFairLock(lockName);
        // 最常见的使用方法
        fairLock.lock();
    }

    /**
     * 加锁
     *
     * @param lockName  锁名称
     * @param leaseTime 锁超时时间
     * @param timeUnit  超时单位
     */
    @Override
    public void lock(String lockName, long leaseTime, TimeUnit timeUnit) {
        RLock fairLock = redissonClient.getFairLock(lockName);
        // 最常见的使用方法
        fairLock.lock(leaseTime, timeUnit);
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    @Override
    public void unLock(String lockName) {
        RLock fairLock = redissonClient.getFairLock(lockName);
        fairLock.unlock();
    }


}
