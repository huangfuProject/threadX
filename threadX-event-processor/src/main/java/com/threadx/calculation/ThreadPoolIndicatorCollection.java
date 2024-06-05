package com.threadx.calculation;

import com.threadx.cache.ThreadPoolIndexCache;
import com.threadx.cache.ThreadPoolIndexData;
import com.threadx.cache.ThreadPoolWeakReferenceCache;
import com.threadx.constant.ThreadXPropertiesEnum;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.description.context.AgentContext;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.publisher.events.ThreadPoolExecutorStatusEvent;
import com.threadx.thread.BusinessThreadXRejectedExecutionHandler;
import com.threadx.thread.ThreadXThreadFactory;
import com.threadx.utils.ThreadXCollectionUtils;
import com.threadx.utils.ThreadXStateEventManager;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * *************************************************<br/>
 * 线程池指标收集<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 17:43
 */
public class ThreadPoolIndicatorCollection {
    private final static ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(1, new ThreadXThreadFactory("threadXIndicatorCollection"));
    private final static AtomicBoolean RUN = new AtomicBoolean(false);
    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ThreadPoolIndicatorCollection.class);

    /**
     * 线程池指标统计
     */
    public static void collection() {
        if (!RUN.compareAndSet(false, true)) {
            return;
        }

        AgentPackageDescription agentPackageDescription = AgentContext.getAgentPackageDescription();
        if (agentPackageDescription == null) {
            logger.error("threadX Meta information parsing failed, the packet is empty, please check whether the installation package is complete! Please refer to the official document deployment!!");
            return;
        }

        Properties envProperties = agentPackageDescription.getEnvProperties();
        String intervalStr = envProperties.getProperty(ThreadXPropertiesEnum.THREAD_POOL_COLLECTION_INTERVAL.getKey(), ThreadXPropertiesEnum.THREAD_POOL_COLLECTION_INTERVAL.getDefaultValue());
        //获取统计间隔
        long interval = Long.parseLong(intervalStr);
        logger.info("Thread pool indicator statistics started. ");
        EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                logger.debug("Thread pool metrics are running.... ");
                Set<ThreadPoolIndexData> allData = ThreadPoolIndexCache.getAllData();
                if (ThreadXCollectionUtils.isNotEmpty(allData)) {
                    allData.forEach(threadPoolIndexData -> {
                        ThreadPoolExecutorStatusEvent event = calculationIndicator(threadPoolIndexData);
                        if (event != null) {
                            logger.debug("The collected information is：{}", event);
                            //发布事件
                            ThreadXStateEventManager.publishEvent(event);
                        }
                    });
                }
            } catch (Throwable e) {
                logger.error("Statistical indicator error with error message: {}", e.getMessage());
            }
        }, interval, interval, TimeUnit.SECONDS);
    }

    /**
     * 计算指标
     *
     * @param indexData 索引
     * @return 线程池的指标
     */
    private static ThreadPoolExecutorStatusEvent calculationIndicator(ThreadPoolIndexData indexData) {
        String threadPoolId = indexData.getThreadPoolId();
        String threadPoolName = indexData.getThreadPoolName();
        String threadPoolGroupName = indexData.getThreadPoolGroupName();
        String createFlow = indexData.getCreateFlow();

        ThreadPoolExecutor executor = ThreadPoolWeakReferenceCache.getCache(threadPoolId);
        if (executor == null) {
            return null;
        }

        ThreadPoolExecutorStatusEvent event = new ThreadPoolExecutorStatusEvent();
        event.setServerName(AgentContext.getServerName());
        event.setInstanceName(AgentContext.getInstanceName());
        event.setThreadPoolName(threadPoolName);
        event.setThreadPoolGroupName(threadPoolGroupName);
        //使用的队列类型
        BlockingQueue<Runnable> queue = executor.getQueue();
        event.setQueueType(queue.getClass().getName());
        //最大的线程数量
        int maximumPoolSize = executor.getMaximumPoolSize();
        event.setMaximumPoolSize(maximumPoolSize);
        //核心的线程数量
        int corePoolSize = executor.getCorePoolSize();
        event.setCorePoolSize(corePoolSize);
        //活跃的线程数量
        int activeCount = executor.getActiveCount();
        event.setActiveCount(activeCount);
        //拒绝策略
        RejectedExecutionHandler rejectedExecutionHandler = executor.getRejectedExecutionHandler();
        //拒绝策略
        Long rejectedCount = -1L;
        if (rejectedExecutionHandler instanceof BusinessThreadXRejectedExecutionHandler) {
            //获取拒绝数量
            rejectedCount = ((BusinessThreadXRejectedExecutionHandler) rejectedExecutionHandler).getCount();
            //获取原始的拒绝策略
            rejectedExecutionHandler = ((BusinessThreadXRejectedExecutionHandler) rejectedExecutionHandler).getRejectedExecutionHandler();
        }
        //设置策略
        event.setRejectedType(rejectedExecutionHandler.getClass().getName());
        //设置拒绝次数
        event.setRejectedCount(rejectedCount);

        //线程过期时间
        long keepAliveTime = executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
        event.setKeepAliveTime(keepAliveTime);
        //完成的任务总量
        long completedTaskCount = executor.getCompletedTaskCount();
        event.setCompletedTaskCount(completedTaskCount);
        //本次进程中，他执行过的任务的总和 包含 未执行的、堆积的、执行中的、执行完成的
        long taskCount = executor.getTaskCount();
        event.setTaskCount(taskCount);
        //曾经达到的最大线程数
        int largestPoolSize = executor.getLargestPoolSize();
        event.setLargestPoolSize(largestPoolSize);
        //当前线程池中的线程数
        int poolSize = executor.getPoolSize();
        event.setThisThreadCount(poolSize);
        event.setThreadPoolFlow(createFlow);
        event.setObjectId(threadPoolId);
        return event;
    }
}
