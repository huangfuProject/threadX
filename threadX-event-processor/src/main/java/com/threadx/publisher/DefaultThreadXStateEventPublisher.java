package com.threadx.publisher;

import com.threadx.thread.ListenerHandlerRejectedExecutionHandler;
import com.threadx.thread.ThreadXThreadFactory;
import com.threadx.utils.SystemUtils;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 默认的事件发布器
 *
 * @author huangfukexing
 * @date 2023/3/17 15:29
 */
public class DefaultThreadXStateEventPublisher extends ThreadXStateEventPublisher<ThreadXStatusEvent> {
    /**
     * 监听器
     */
    private final Set<StatusEventListener<?>> LISTENERS = new CopyOnWriteArraySet<>();

    /**
     * 执行事件监听处理器的线程池
     */
    private final static ThreadPoolExecutor EVENT_HANDLER_THREAD_POOL = new ThreadPoolExecutor(SystemUtils.getSystemCoreCount(), SystemUtils.getSystemCoreCount() * 2, 60, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1024), new ThreadXThreadFactory("threadX-event-listener"), new ListenerHandlerRejectedExecutionHandler());


    @Override
    public void publishEvent(ThreadXStatusEvent event) {
        for (StatusEventListener<?> listener : LISTENERS) {
            //对比 监听的事件类型是否是一样的
            Class<?> eventType = listener.eventType();
            //一样就提交任务
            if (event.getClass() == eventType) {
                EVENT_HANDLER_THREAD_POOL.execute(() -> listener.listener(event));
            }

        }
    }

    @Override
    public void addListener(StatusEventListener<?> statusEventListener) {
        LISTENERS.add(statusEventListener);
    }

    @Override
    public void unListener(StatusEventListener<?> statusEventListener) {
        LISTENERS.remove(statusEventListener);
    }
}
