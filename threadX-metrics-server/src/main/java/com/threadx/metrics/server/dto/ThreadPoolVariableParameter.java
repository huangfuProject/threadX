package com.threadx.metrics.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池可变参数
 *
 * @author huangfukexing
 * @date 2023/8/23 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolVariableParameter implements Serializable {
    private static final long serialVersionUID = -5734601191342543479L;

    /**
     * 线程池的id
     */
    private Long threadPoolId;

    /**
     * 线程池核心数量
     */
    private Integer coreSize;

    /**
     * 线程池最大数量
     */
    private Integer maximumPoolSize;

    /**
     * 线程池空闲时间  单位毫秒
     */
    private Long keepAliveTime;
    /**
     * 线程池拒绝策略全限定名
     */
    private String rejectedExecutionHandlerClass;
}
