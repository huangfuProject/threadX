package com.threadx.cache;


import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.utils.ThreadPoolEmptyUtils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 持有线程池弱引用的缓冲池
 *
 * @author huangfukexing
 * @date 2023/3/17 12:00
 */
public class ThreadPoolWeakReferenceCache {

    private final static Map<String, WeakReference<ThreadPoolExecutor>> THREAD_POOL_WEAK_REFERENCE_CACHE = new ConcurrentHashMap<>();

    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ThreadPoolWeakReferenceCache.class);
    /**
     * 设置缓存 包装完成之后
     *
     * @param threadPoolExecutor 线程池
     */
    public static ThreadPoolIndexData setCache(ThreadPoolExecutor threadPoolExecutor) {
        //缓存索引信息
        ThreadPoolIndexData threadPoolIndexData = ThreadPoolIndexCache.setCache(threadPoolExecutor);
        //缓存线程池信息
        WeakReference<ThreadPoolExecutor> threadPoolExecutorWeakReference = new WeakReference<>(threadPoolExecutor);
        THREAD_POOL_WEAK_REFERENCE_CACHE.put(threadPoolIndexData.getThreadPoolId(), threadPoolExecutorWeakReference);
        //返回索引信息
        return threadPoolIndexData;
    }

    /**
     * 获取缓存的线程池
     *
     * @param cacheKey 缓存主键
     * @return 缓存值
     */
    public static ThreadPoolExecutor getCache(String cacheKey) {

        ThreadPoolIndexData threadPoolIndexData = ThreadPoolIndexCache.getCache(cacheKey);
        if (threadPoolIndexData != null) {

            WeakReference<ThreadPoolExecutor> threadPoolExecutorWeakReference = THREAD_POOL_WEAK_REFERENCE_CACHE.get(cacheKey);

            //缓存错误
            if (threadPoolExecutorWeakReference == null) {
                logger.error("thread pool cache error, is thread pool group name is {}, thread pool name is {}", threadPoolIndexData.getThreadPoolGroupName(), threadPoolIndexData.getThreadPoolName());
                ThreadPoolEmptyUtils.cleanCache(cacheKey);
                return null;
            }

            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorWeakReference.get();
            //被回收
            if (threadPoolExecutor == null) {
                logger.info("The thread pool is gc, and the collected information thread pool group name is {}, thread pool name is {}", threadPoolIndexData.getThreadPoolGroupName(), threadPoolIndexData.getThreadPoolName());
                ThreadPoolEmptyUtils.cleanCache(cacheKey);
                return null;
            }
            // 已关闭
            if (threadPoolExecutor.isShutdown()) {
                logger.info("The thread pool has been closed, thread pool group name is {}, thread pool name is {}", threadPoolIndexData.getThreadPoolGroupName(), threadPoolIndexData.getThreadPoolName());
                ThreadPoolEmptyUtils.cleanCache(cacheKey);
                return null;
            }
            return threadPoolExecutor;
        } else {
            logger.warn("No thread pool index information found.");
        }
        return null;

    }

    /**
     * 删除缓存
     *
     * @param cacheKey 缓存主键
     */
    public static void removeCache(String cacheKey) {
        THREAD_POOL_WEAK_REFERENCE_CACHE.remove(cacheKey);
    }
}
