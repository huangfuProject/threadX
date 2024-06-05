package com.threadx.state;

import com.threadx.cache.ThreadPoolIndexCache;
import com.threadx.cache.ThreadPoolIndexData;
import com.threadx.cache.ThreadPoolTaskCache;
import com.threadx.description.context.AgentContext;
import com.threadx.listeners.ThreadPoolTaskEventListener;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.publisher.events.ThreadPoolExecutorThreadTaskState;
import com.threadx.thread.BusinessThreadXRunnable;
import com.threadx.utils.ThreadXStateEventManager;
import com.threadx.utils.ThreadXThreadPoolUtil;
import com.threadx.utils.ThreadXThrowableMessageUtil;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 线程池任务
 *
 * @author huangfukexing
 * @date 2023/3/22 14:00
 */
public class ThreadPoolTaskState {

    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ThreadPoolExecutorState.class);
    private static final AtomicBoolean ADD_LISTENERS_STATUS = new AtomicBoolean(false);
    /**
     * 初始化任务，调用时机是任务刚被提交的时候
     *
     * @param sourceCommand 提交的任务
     * @param executorPool  使用的线程池
     */
    public static Runnable init(Runnable sourceCommand, ThreadPoolExecutor executorPool) {
        if(ADD_LISTENERS_STATUS.compareAndSet(false, true)) {
            ThreadXStateEventManager.addListener(new ThreadPoolTaskEventListener());
        }
        BusinessThreadXRunnable command = new BusinessThreadXRunnable(sourceCommand);

        try {
            String threadPoolId = ThreadXThreadPoolUtil.getObjectId(executorPool);
            ThreadPoolIndexData threadPoolIndexData = ThreadPoolIndexCache.getCache(threadPoolId);
            if (threadPoolIndexData != null) {

                String threadPoolName = threadPoolIndexData.getThreadPoolName();
                String threadPoolGroupName = threadPoolIndexData.getThreadPoolGroupName();
                logger.debug("thread pool {} submit task.", threadPoolGroupName);
                //构建数据
                ThreadPoolExecutorThreadTaskState threadPoolExecutorThreadTaskState = new ThreadPoolExecutorThreadTaskState();
                threadPoolExecutorThreadTaskState.setServerName(AgentContext.getServerName());
                threadPoolExecutorThreadTaskState.setInstanceName(AgentContext.getInstanceName());
                threadPoolExecutorThreadTaskState.setSubmitTime(System.currentTimeMillis());
                String runnableId = ThreadXThreadPoolUtil.getObjectId(command);
                threadPoolExecutorThreadTaskState.setTaskId(runnableId);
                threadPoolExecutorThreadTaskState.setThreadPoolName(threadPoolName);
                threadPoolExecutorThreadTaskState.setThreadPoolGroupName(threadPoolGroupName);
                threadPoolExecutorThreadTaskState.setThreadPoolId(threadPoolId);
                //缓存数据
                ThreadPoolTaskCache.addCache(runnableId, threadPoolExecutorThreadTaskState);
                logger.debug("thread task init cache success.");
            }
        } catch (Throwable e) {
            logger.error("threadX task init error. {}", ThreadXThrowableMessageUtil.messageRead(e));
        }
        return command;
    }

    /**
     * 任务执行的前置回调
     *
     * @param t 执行任务的线程
     * @param r 被执行的任务
     */
    public static void beforeTaskExecution(Thread t, Runnable r) {
        try {
            String taskId = ThreadXThreadPoolUtil.getObjectId(r);
            ThreadPoolExecutorThreadTaskState threadPoolExecutorThreadTaskState = ThreadPoolTaskCache.getCache(taskId);
            if (threadPoolExecutorThreadTaskState != null) {
                //设置任务的开始时间
                threadPoolExecutorThreadTaskState.setStartTime(System.currentTimeMillis());
                //设置线程名
                threadPoolExecutorThreadTaskState.setThreadName(t.getName());
                //设置没有被拒绝
                threadPoolExecutorThreadTaskState.setRefuse(false);
            }
        } catch (Throwable e) {
            logger.error("threadX task beforeTaskExecution error. {}", ThreadXThrowableMessageUtil.messageRead(e));
        }
    }


    /**
     * 任务执行的后置回调
     *
     * @param r 执行完成的任务
     * @param t 异常信息
     */
    public static void afterTaskExecution(Runnable r, Throwable t) {
        String taskId = ThreadXThreadPoolUtil.getObjectId(r);
        ThreadPoolExecutorThreadTaskState threadPoolExecutorThreadTaskState = ThreadPoolTaskCache.getCache(taskId);
        try {
            if (threadPoolExecutorThreadTaskState != null) {
                if (t != null) {
                    //任务执行失败
                    threadPoolExecutorThreadTaskState.setSuccess(false);
                    //异常信息
                    threadPoolExecutorThreadTaskState.setThrowable(t);
                } else {
                    //任务执行成功
                    threadPoolExecutorThreadTaskState.setSuccess(true);
                }
                //任务的结束时间
                threadPoolExecutorThreadTaskState.setEndTime(System.currentTimeMillis());
                //计算任务耗时
                threadPoolExecutorThreadTaskState.computingTime();
                //设置缓存
                ThreadXStateEventManager.publishEvent(threadPoolExecutorThreadTaskState);
            }
        } catch (Throwable e) {
            logger.error("threadX task beforeTaskExecution error. {}", ThreadXThrowableMessageUtil.messageRead(e));
        } finally {
            if (threadPoolExecutorThreadTaskState != null) {
                logger.debug("task run end. {}", threadPoolExecutorThreadTaskState);

                ThreadPoolTaskCache.removeCache(taskId);
            }

        }
    }
}
