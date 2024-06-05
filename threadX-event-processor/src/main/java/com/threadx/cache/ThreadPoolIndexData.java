package com.threadx.cache;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 线程池索引数据对象
 *
 * @author huangfukexing
 * @date 2023/3/17 10:54
 */
@Getter
@EqualsAndHashCode
public class ThreadPoolIndexData {

    private final String threadPoolId;
    /**
     * 线程池的名称
     */
    private final String threadPoolName;

    /**
     * 线程池的组的名称
     */
    private final String threadPoolGroupName;

    /**
     * 创建流
     */
    private final String createFlow;

    public ThreadPoolIndexData(String threadPoolId, String threadPoolName, String threadPoolGroupName, String createFlow) {
        this.threadPoolId = threadPoolId;
        this.threadPoolName = threadPoolName;
        this.threadPoolGroupName = threadPoolGroupName;
        this.createFlow = createFlow;
    }
}
