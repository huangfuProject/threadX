package com.threadx.listeners;

import com.threadx.description.context.AgentContext;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.publisher.StatusEventListener;
import com.threadx.publisher.events.ThreadPoolExecutorThreadTaskState;

/**
 * 线程池任务指标收集器
 *
 * @author huangfukexing
 * @date 2023/3/29 14:45
 */
public class ThreadPoolTaskEventListener implements StatusEventListener<ThreadPoolExecutorThreadTaskState> {


    private final static MetricsOutApi METRICS;

    static {
        METRICS = AgentContext.getMetrics();
    }

    @Override
    public void doListener(ThreadPoolExecutorThreadTaskState event) {
        METRICS.outThreadTaskMetricsData(event);
    }

    @Override
    public Class<ThreadPoolExecutorThreadTaskState> eventType() {
        return ThreadPoolExecutorThreadTaskState.class;
    }
}
