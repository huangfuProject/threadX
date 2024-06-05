package com.threadx.metrics.server.constant;

/**
 * redis key的常数
 *
 * @author huangfukexing
 * @date 2023/4/21 15:09
 */
public interface RedisKeyConstant {
    /**
     * 线程池数据集合
     */
    String THREAD_POOL_DATA = "threadx.thread.pool.list";

    /**
     * 线程池 任务数据集合
     */
    String THREAD_TASK_DATA = "threadx.thread.task.list";
}
