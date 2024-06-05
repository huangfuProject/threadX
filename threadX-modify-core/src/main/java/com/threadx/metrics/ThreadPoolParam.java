package com.threadx.metrics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池相关参数
 *
 * @author huangfukexing
 * @date 2023/8/18 15:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolParam implements Serializable {
    private static final long serialVersionUID = -6792025673719478154L;

    /**
     * 线程池对象的Id
     */
    private String objectId;

    /**
     * 线程池核心数量
     */
    private Integer coreSize;

    /**
     * 线程池最大数量
     */
    private Integer maximumPoolSize;

    /**
     * 线程池空闲时间  单位纳秒
     */
    private Long keepAliveTime;

    /**
     * 线程池工厂全限定名
     */
    private String threadFactoryClass;

    /**
     * 线程池拒绝策略全限定名
     */
    private String rejectedExecutionHandlerClass;

}
