package com.threadx.metrics.tms;

import cn.hutool.core.bean.BeanUtil;
import com.threadx.communication.server.CommunicationServerBootStrap;
import com.threadx.communication.server.ServerSendMessage;
import com.threadx.communication.server.cache.ConnectionCache;
import com.threadx.communication.server.config.ServerConfig;
import com.threadx.description.context.AgentContext;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.metrics.ThreadPoolExecutorData;
import com.threadx.metrics.ThreadTaskExecutorData;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.metrics.tms.config.ThreadXMetricsTmsPropertiesEnum;
import com.threadx.metrics.tms.handler.server.ThreadPoolUpdateRequestHandler;
import com.threadx.metrics.tms.packages.ThreadPoolDataPacket;
import com.threadx.metrics.tms.packages.ThreadPoolTaskDataPacket;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 指标输出器
 *
 * @author huangfukexing
 * @date 2025/4/28 17:42
 */
public class TmsMetricsOut implements MetricsOutApi {

    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(TmsMetricsOut.class);


    private final AtomicInteger THREAD_IDX = new AtomicInteger();

    private CommunicationServerBootStrap communicationServerBootStrap = null;

    // 消费者线程工厂
    private final ThreadFactory threadFactory = r -> new Thread(r, "ThreadX-Disruptor-Consumer-" + THREAD_IDX.getAndIncrement());

    // 线程池数据队列
    private DisruptorManager<ThreadPoolDataPacket> poolManager = null;

    // 任务数据队列
    private DisruptorManager<ThreadPoolTaskDataPacket> taskManager = null;


    @Override
    public void init() {
        String host = AgentContext.getAgentPackageDescription().getEnvProperties().getProperty(ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_HOST.getKey());
        String port = AgentContext.getAgentPackageDescription().getEnvProperties().getProperty(ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_PORT.getKey());
        if (ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_HOST.getDefaultValue().equalsIgnoreCase(host)) {
            logger.error("Indicator collector initialization failed. Please check the configuration: {}", ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_HOST.getKey());
            throw new RuntimeException("Indicator collector initialization failed. Please check the configuration.");
        }

        if (ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_PORT.getDefaultValue().equalsIgnoreCase(port)) {
            logger.error("Indicator collector initialization failed. Please check the configuration: {}", ThreadXMetricsTmsPropertiesEnum.THREADX_METRICS_OUT_TMS_PORT.getKey());
            throw new RuntimeException("Indicator collector initialization failed. Please check the configuration.");
        }
        ServerConfig serverConfig = new ServerConfig(host, Integer.parseInt(port));
        serverConfig.addChannelInboundHandler("ThreadPoolUpdateRequestHandler", new ThreadPoolUpdateRequestHandler());
        communicationServerBootStrap = new CommunicationServerBootStrap(serverConfig);
        communicationServerBootStrap.startServer();

        poolManager = new DisruptorManager<>((event, sequence, endOfBatch) -> {
            ServerSendMessage.asyncSendMessage(event.getData(), serverConfig);


        }, threadFactory);

        taskManager = new DisruptorManager<>((event, sequence, endOfBatch) -> {
            ServerSendMessage.asyncSendMessage(event.getData(), serverConfig);
        }, threadFactory);

    }

    @Override
    public void outThreadPoolMetricsData(ThreadPoolExecutorData metricsData) {
        ThreadPoolDataPacket threadPoolDataPacket = new ThreadPoolDataPacket();
        BeanUtil.copyProperties(metricsData, threadPoolDataPacket);
        poolManager.publish(threadPoolDataPacket);
    }

    @Override
    public void outThreadTaskMetricsData(ThreadTaskExecutorData metricsData) {
        ThreadPoolTaskDataPacket threadPoolTaskDataPacket = new ThreadPoolTaskDataPacket();
        BeanUtil.copyProperties(metricsData, threadPoolTaskDataPacket);
        taskManager.publish(threadPoolTaskDataPacket);
    }

    @Override
    public void destroy() {
        if (poolManager != null) {
            poolManager.shutdown();
        }
        if (taskManager != null) {
            taskManager.shutdown();
        }
        if(communicationServerBootStrap != null) {
            try {
                communicationServerBootStrap.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getMetricsName() {
        return "tms";
    }
}
