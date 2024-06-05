package com.threadx.metrics;

import lombok.*;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程池采集的数据<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/9 15:01
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ThreadPoolExecutorData implements Serializable {
    private static final long serialVersionUID = 6070002614529821970L;
    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 线程池的名称
     */
    private String threadPoolName;

    /**
     * 线程池的组的名称
     */
    private String threadPoolGroupName;

    /**
     * 核心线程池的数量
     */
    private Integer corePoolSize;

    /**
     * 最大可执行任务的线程数
     */
    private Integer maximumPoolSize;

    /**
     * 当前活跃的线程数
     * 当前正在执行任务的线程数量，不包括已创建但尚未执行任务的线程
     */
    private Integer activeCount;

    /**
     * 当前线程池的线程数量  包含没有执行任务的线程还没有来得及被销毁的非核心线程
     * 包括已创建但尚未执行任务的线程，以及正在执行任务的线程，即使它们是空闲的。
     */
    private Integer thisThreadCount;


    /**
     * 曾将达到的最大的线程数  历史信息
     */
    private Integer largestPoolSize;

    /**
     * 拒绝执行的次数
     */
    private Long rejectedCount;

    /**
     * 堆积的、执行中的、已经完成的任务的总和
     */
    private Long taskCount;


    /**
     * 已经完成的数量
     */
    private Long completedTaskCount;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * 拒绝策略
     */
    private String rejectedType;
    /**
     * 毫秒
     * 线程空闲
     */
    private Long keepAliveTime;

    /**
     * 线程池的创建流程
     */
    private String threadPoolFlow;

    /**
     * 线程池id  objectId
     */
    private String objectId;
}
