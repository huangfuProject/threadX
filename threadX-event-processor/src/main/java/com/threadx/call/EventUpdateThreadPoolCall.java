package com.threadx.call;

import com.threadx.cache.ThreadPoolWeakReferenceCache;
import com.threadx.metrics.ThreadPoolParam;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池修改回调
 *
 * @author huangfukexing
 * @date 2023/8/18 15:25
 */
public class EventUpdateThreadPoolCall implements UpdateThreadPoolCall{
    @Override
    public void modify(ThreadPoolParam threadPoolParam) throws Exception {
        if(threadPoolParam == null) {
            throw new RuntimeException("参数非法.");
        }
        String objectId = threadPoolParam.getObjectId();
        if(objectId == null || "".equalsIgnoreCase(objectId)) {
            throw new RuntimeException("参数非法.");
        }
        //获取对应的线程池
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolWeakReferenceCache.getCache(objectId);
        if(threadPoolExecutor == null) {
            throw new RuntimeException("线程池不存在或者已经被jvm回收.");
        }

        Integer maximumPoolSize = threadPoolParam.getMaximumPoolSize();
        Integer coreSize = threadPoolParam.getCoreSize();
        String rejectedExecutionHandlerClass = threadPoolParam.getRejectedExecutionHandlerClass();
        Long keepAliveTime = threadPoolParam.getKeepAliveTime();


        if(maximumPoolSize == null){
            maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
        }

        if(coreSize == null){
            coreSize = threadPoolExecutor.getCorePoolSize();
        }


        if(keepAliveTime == null || keepAliveTime <= 0){
            keepAliveTime = threadPoolExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS);
        }

        if(coreSize > maximumPoolSize) {
            throw new RuntimeException("线程池参数不合法【coreSize > maximumPoolSize】.");
        }

        if(rejectedExecutionHandlerClass != null && !"".equals(rejectedExecutionHandlerClass)) {

        }

        threadPoolExecutor.setCorePoolSize(coreSize);
        threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
        threadPoolExecutor.setKeepAliveTime(keepAliveTime, TimeUnit.MILLISECONDS);
    }
}
