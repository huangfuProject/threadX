package com.threadx.publisher;

/**
 * 事件监听者接口
 *
 * @author huangfukexing
 * @date 2023/3/17 13:53
 */
public interface StatusEventListener<T extends ThreadXStatusEvent> {

    /**
     * 监听对象
     *
     * @param event 事件源
     */
    default void listener(Object event) {
        doListener((T)event);
    }

    /**
     * 监听对象
     *
     * @param event 事件源
     */
    void doListener(T event);

    /**
     * 返回要监听的事件源的类型  懒得反射去拿泛型  字节写吧
     *
     * @return 事件源的类型
     */
    Class<T> eventType();
}
