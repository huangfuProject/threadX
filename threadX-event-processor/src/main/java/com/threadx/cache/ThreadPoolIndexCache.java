package com.threadx.cache;

import com.threadx.utils.ThreadXThreadPoolUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池索引缓存
 *
 * @author huangfukexing
 * @date 2023/3/17 10:06
 */
public class ThreadPoolIndexCache {

    /**
     * 存储线程池的索引信息
     * 索引Key使用 {@link System#identityHashCode(Object)
     */
    private final static Map<String, ThreadPoolIndexData> INDEX_CACHE = new ConcurrentHashMap<>();

    /**
     * 设置缓存
     *
     * @param executor 线程池
     * @return 返回索引好的数据
     */
    public static ThreadPoolIndexData setCache(ThreadPoolExecutor executor) {
        String groupName = ThreadXThreadPoolUtil.generateThreadPoolGroupName();
        String threadPoolName = ThreadXThreadPoolUtil.generateThreadPoolName(groupName, executor);
        String threadPoolId = ThreadXThreadPoolUtil.getObjectId(executor);
        String createFlow = ThreadXThreadPoolUtil.getCreateFlow();
        return ThreadPoolIndexCache.setCache(threadPoolId, new ThreadPoolIndexData(threadPoolId, threadPoolName, groupName, createFlow));
    }

    /**
     * 获取缓存数据
     *
     * @param executor 线程池
     * @return 线程池的索引信息
     */
    public static ThreadPoolIndexData getCache(ThreadPoolExecutor executor) {
        return ThreadPoolIndexCache.getCache(ThreadXThreadPoolUtil.getObjectId(executor));
    }


    /**
     * 获取所有的索引数据
     *
     * @return 返回所有的索引数据
     */
    public static Set<ThreadPoolIndexData> getAllData() {
        return new HashSet<>(INDEX_CACHE.values());
    }

    /**
     * 删除索引信息
     *
     * @param executor 执行器信息
     * @return 索引信息
     */
    public static ThreadPoolIndexData removeCache(ThreadPoolExecutor executor) {
        return ThreadPoolIndexCache.removeCache(ThreadXThreadPoolUtil.getObjectId(executor));
    }

    /**
     * 设置索引信息
     *
     * @param cacheKey            索引key
     * @param threadPoolIndexData 索引值
     * @return 索引值
     */
    public static ThreadPoolIndexData setCache(String cacheKey, ThreadPoolIndexData threadPoolIndexData) {
        INDEX_CACHE.put(cacheKey, threadPoolIndexData);
        return threadPoolIndexData;
    }

    /**
     * 获取索引
     *
     * @param cacheKey 索引Key
     * @return 索引值
     */
    public static ThreadPoolIndexData getCache(String cacheKey) {
        return INDEX_CACHE.get(cacheKey);
    }

    /**
     * 删除索引
     *
     * @param cacheKey 索引key
     * @return 索引值
     */
    public static ThreadPoolIndexData removeCache(String cacheKey) {
        return INDEX_CACHE.remove(cacheKey);
    }
}
