package com.threadx.metrics.server.lock;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁模板引擎
 *
 * @author huangfu
 * @date 2020年12月23日16:44:57
 */
public interface DistributedLockTemplate extends Serializable {

    /**
     * 获取锁（如果有）并立即返回true值。 如果锁不可用，则此方法将立即返回false值。
     *
     * @param lockName 锁名称
     * @return 是否获取锁成功
     */
    boolean tryLock(String lockName);

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
    boolean tryLock(String lockName, long maxWaitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 尝试获取具有定义的leaseTime的锁。 如有必要，等待定义的maxWaitTime直到锁可用。
     * 该锁没有超时时间
     *
     * @param lockName    锁名称
     * @param maxWaitTime 最大等待时间
     * @param timeUnit 最大等待时间单位
     * @return 是否加锁成功
     * @throws InterruptedException 中断错误
     */
    boolean tryLock(String lockName, long maxWaitTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 加锁 基于锁名称
     * 不得到锁不罢休
     *
     * @param lockName 锁名称
     */
    void lock(String lockName);

    /**
     * 加锁
     *
     * @param lockName  锁名称
     * @param leaseTime 锁超时时间
     * @param timeUnit  超时单位
     */
    void lock(String lockName, long leaseTime, TimeUnit timeUnit);

    /**
     * 解锁
     *
     * @param lockName 锁名称
     */
    void unLock(String lockName);
}
