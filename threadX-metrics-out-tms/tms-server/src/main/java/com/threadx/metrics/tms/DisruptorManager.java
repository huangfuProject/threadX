package com.threadx.metrics.tms;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ThreadFactory;
/**
 * 指标输出器
 *
 * @author huangfukexing
 * @date 2025/4/28 17:42
 */
public class DisruptorManager<T> {
    private final Disruptor<DisruptorEvent<T>> disruptor;
    private final RingBuffer<DisruptorEvent<T>> ringBuffer;

    public DisruptorManager(
            EventHandler<DisruptorEvent<T>> handler, 
            ThreadFactory threadFactory) {
        // 1. 初始化Disruptor
        this.disruptor = new Disruptor<>(
                DisruptorEvent::new,
                // 固定容量
                4096,
                threadFactory,
                // 支持多生产者
                ProducerType.MULTI,
                // 阻塞策略
                new BlockingWaitStrategy()
        );

        // 2. 绑定消费者
        disruptor.handleEventsWith(handler);
        
        // 3. 启动RingBuffer
        this.ringBuffer = disruptor.start();
    }

    // 发布数据（线程安全）
    public void publish(T data) {
        long sequence = ringBuffer.next();
        try {
            DisruptorEvent<T> event = ringBuffer.get(sequence);
            event.setData(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    // 关闭队列
    public void shutdown() {
        disruptor.shutdown();
    }
}