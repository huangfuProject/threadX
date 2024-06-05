package com.threadx.publisher;

/**
 * 线程池状态的事件发布器
 *
 * @author huangfukexing
 * @date 2023/3/17 13:49
 */
public abstract class ThreadXStateEventPublisher<T extends ThreadXStatusEvent> {

    /**
     * 发布一个事件
     *
     * @param event 事件
     */
    public abstract void publishEvent(T event);

    /**
     * 添加一个监听
     *
     * @param statusEventListener 添加监听
     */
    public abstract void addListener(StatusEventListener<?> statusEventListener);

    /**
     * 移除一个监听
     *
     * @param statusEventListener 监听器
     */
    public abstract void unListener(StatusEventListener<?> statusEventListener);
}
