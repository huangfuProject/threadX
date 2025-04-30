package com.threadx.thread;

import lombok.Getter;

import java.util.concurrent.ThreadFactory;

/**
 * 业务线程池线程工厂封装
 *
 * @author huangfukexing
 * @date 2025/4/30 13:54
 */
@Getter
public class BusinessThreadXThreadFactory implements ThreadFactory {
    protected final ThreadFactory businessThreadFactory;
    protected final String threadPoolName;

    public BusinessThreadXThreadFactory(ThreadFactory businessThreadFactory) {
        this.businessThreadFactory = businessThreadFactory;
        threadPoolName = businessThreadFactory.newThread(()->{}).getName();
    }

    @Override
    public Thread newThread(Runnable r) {
        return businessThreadFactory.newThread(r);
    }

}
