package com.threadx.utils;

import com.threadx.publisher.DefaultThreadXStateEventPublisher;
import com.threadx.publisher.StatusEventListener;
import com.threadx.publisher.ThreadXStateEventPublisher;
import com.threadx.publisher.ThreadXStatusEvent;

/**
 * *************************************************<br/>
 * 事件管理器<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 16:56
 */
public class ThreadXStateEventManager {
    /**
     * 事件发布器
     */
    private final static ThreadXStateEventPublisher<ThreadXStatusEvent> THREADX_STATE_EVENT_PUBLISHER = new DefaultThreadXStateEventPublisher();

    /**
     * 发布一个事件
     *
     * @param event 事件源
     */
    public static void publishEvent(ThreadXStatusEvent event) {
        THREADX_STATE_EVENT_PUBLISHER.publishEvent(event);
    }

    /**
     * 添加监听
     *
     * @param listener 监听者
     */
    public static void addListener(StatusEventListener<? extends ThreadXStatusEvent> listener) {
        THREADX_STATE_EVENT_PUBLISHER.addListener(listener);
    }

    /**
     * 删除监听
     *
     * @param listener 删除监听
     */
    public static void unListener(StatusEventListener<? extends ThreadXStatusEvent> listener) {
        THREADX_STATE_EVENT_PUBLISHER.unListener(listener);
    }

    /**
     * 返回一个事件发布器
     *
     * @return 时间发布器
     */
    public static ThreadXStateEventPublisher<? extends ThreadXStatusEvent> getThreadXStatusEvent() {
        return new DefaultThreadXStateEventPublisher();
    }
}
