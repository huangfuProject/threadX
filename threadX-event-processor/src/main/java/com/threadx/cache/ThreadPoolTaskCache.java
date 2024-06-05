package com.threadx.cache;

import com.threadx.publisher.events.ThreadPoolExecutorThreadTaskState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 线程池的任务缓存信息
 *
 * @author huangfukexing
 * @date 2023/3/22 14:05
 */
public class ThreadPoolTaskCache {

    /**
     * 任务信息的缓冲池
     */
    private final static Map<String, ThreadPoolExecutorThreadTaskState> TASK_POOL_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 添加一个缓冲
     *
     * @param taskId                            任务的id
     * @param threadPoolExecutorThreadTaskState 任务的元信息
     */
    public static void addCache(String taskId, ThreadPoolExecutorThreadTaskState threadPoolExecutorThreadTaskState) {
        TASK_POOL_CACHE.put(taskId, threadPoolExecutorThreadTaskState);
    }


    /**
     * 删除缓存
     *
     * @param taskId 任务的id
     */
    public static void removeCache(String taskId) {
        TASK_POOL_CACHE.remove(taskId);
    }

    /**
     * 获取缓存
     *
     * @param taskId 任务的id
     * @return 线程池的元信息
     */
    public static ThreadPoolExecutorThreadTaskState getCache(String taskId) {
        return TASK_POOL_CACHE.get(taskId);
    }


}
