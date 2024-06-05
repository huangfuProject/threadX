package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.threadx.communication.common.agreement.packet.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池数据的采集信息
 *
 * @author huangfukexing
 * @date 2023/4/7 13:31
 */

@TableName("thread_pool_data")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolData extends BaseEntity {
    private static final long serialVersionUID = 3801673501485117601L;

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
     */
    private Integer activeCount;

    /**
     * 当前线程池的线程数量  包含没有执行任务的线程还没有来得及被销毁的非核心线程
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
     * 服务的名称
     */
    private String serverKey;
    /**
     * 实例名称
     */
    private String instanceKey;

    /**
     * 采集的机器
     */
    private String address;

    /**
     * 实例id
     */
    private Long instanceId;


    /**
     * 线程池对象Id
     */
    private String threadPoolObjectId;
}
