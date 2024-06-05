package com.threadx.utils;

import com.threadx.cache.ThreadPoolIndexCache;
import com.threadx.cache.ThreadPoolWeakReferenceCache;

/**
 * 线程池清空动作
 *
 * @author huangfukexing
 * @date 2023/3/17 12:26
 */
public class ThreadPoolEmptyUtils {

    /**
     * 清理缓存
     *
     * @param cacheKey 缓存主键
     */
    public static void cleanCache(String cacheKey) {
        //删除缓存索引
        ThreadPoolIndexCache.removeCache(cacheKey);
        //删除线程池缓存
        ThreadPoolWeakReferenceCache.removeCache(cacheKey);
    }
}
