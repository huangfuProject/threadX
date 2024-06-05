package com.threadx.thread;

import com.threadx.cache.ThreadPoolTaskCache;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.publisher.events.ThreadPoolExecutorThreadTaskState;
import com.threadx.utils.ThreadXStateEventManager;
import com.threadx.utils.ThreadXThreadPoolUtil;
import com.threadx.utils.ThreadXThrowableMessageUtil;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * *************************************************<br/>
 * 业务线程使用的拒绝策略包装<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:11
 */
public class BusinessThreadXRejectedExecutionHandler implements RejectedExecutionHandler {
    private final RejectedExecutionHandler rejectedExecutionHandler;
    private final AtomicLong COUNT;
    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(BusinessThreadXRejectedExecutionHandler.class);

    public BusinessThreadXRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
        this.COUNT = new AtomicLong(0);
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        //计算任务的id
        String taskId = ThreadXThreadPoolUtil.getObjectId(r);
        try {
            //拒绝策略执行的前置操作
            rejectTaskInformationSupplementBefore(taskId);
            //执行用户的拒绝策略
            rejectedExecutionHandler.rejectedExecution(r, executor);
        } finally {
            //拒绝计数
            COUNT.incrementAndGet();
            //拒绝策略的后置操作
            rejectTaskInformationSupplementAfter(taskId);
        }

    }

    /**
     * 拒绝任务前的指标补充操作
     *
     * @param taskId 需要补充指标的任务的id
     */
    public void rejectTaskInformationSupplementBefore(String taskId) {
        //获取日志信息
        try {
            //获取 线程任务的缓存信息
            ThreadPoolExecutorThreadTaskState cache = ThreadPoolTaskCache.getCache(taskId);
            if (cache != null) {
                //设置一个任务的开始时间
                cache.setStartTime(System.currentTimeMillis());
            }
        } catch (Throwable e) {
            logger.error("method:rejectTaskInformationSupplementBefore. Statistical task rejection information is abnormal. Procedure. error message: {}", ThreadXThrowableMessageUtil.messageRead(e));
        }
    }

    /**
     * 拒绝任务后的指标补充操作
     *
     * @param taskId 需要补充指标的任务的id
     */
    public void rejectTaskInformationSupplementAfter(String taskId) {
        try {
            ThreadPoolExecutorThreadTaskState cache = ThreadPoolTaskCache.getCache(taskId);
            if (cache != null) {
                //设置任务的结束时间
                cache.setEndTime(System.currentTimeMillis());
                //任务被拒绝策略执行
                cache.setRefuse(true);
                //任务执行失败
                cache.setSuccess(false);
                //计算耗时指标信息
                cache.computingTime();
                logger.debug("thread pool: {} Refuse task runing. The rejection policy is {}. task Index value {}, The number of times the execution is rejected is {}", cache.getThreadPoolName(), rejectedExecutionHandler.getClass().getName(), cache, getCount());
                //设置缓存
                ThreadXStateEventManager.publishEvent(cache);
            }

        } catch (Throwable e) {
            logger.error("Statistical task rejection information is abnormal. Procedure. error message: {}", ThreadXThrowableMessageUtil.messageRead(e));
        } finally {
            //删除任务缓存
            ThreadPoolTaskCache.removeCache(taskId);
        }

    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

    public Long getCount() {
        return COUNT.get();
    }

    @Override
    public String toString() {
        return rejectedExecutionHandler.toString();
    }
}
