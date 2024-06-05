package com.threadx.listeners;

import com.threadx.description.context.AgentContext;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.publisher.StatusEventListener;
import com.threadx.publisher.events.ThreadPoolExecutorStatusEvent;

/**
 * 线程池相关指标事件监听机制
 *
 * @author huangfukexing
 * @date 2023/3/29 14:29
 */
public class ThreadPoolEventListener implements StatusEventListener<ThreadPoolExecutorStatusEvent> {


    private final static MetricsOutApi METRICS;

    static {
        METRICS = AgentContext.getMetrics();
    }


    @Override
    public void doListener(ThreadPoolExecutorStatusEvent event) {
        METRICS.outThreadPoolMetricsData(event);
    }

    @Override
    public Class<ThreadPoolExecutorStatusEvent> eventType() {
        return ThreadPoolExecutorStatusEvent.class;
    }
}
