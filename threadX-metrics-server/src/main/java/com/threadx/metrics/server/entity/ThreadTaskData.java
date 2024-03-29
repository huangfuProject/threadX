package com.threadx.metrics.server.entity;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.threadx.communication.common.agreement.packet.ThreadPoolTaskCollectMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池任务数据异步处理器
 *
 * @author huangfukexing
 * @date 2023/4/18 12:42
 */
@TableName("thread_task_data")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadTaskData extends BaseEntity {
    private static final long serialVersionUID = -7278833284773448620L;
    /**
     * 线程池组的名称
     */
    private String threadPoolGroupName;

    /**
     * 线程池的Id
     */
    private String threadPoolObjectId;

    /**
     * 线程池的名称
     */
    private String threadPoolName;

    /**
     * 被那一个线程执行的
     */
    private String threadName;

    /**
     * 该时间为任务被提交的时间，只要该任务被加载进线程池，这个时间就会被初始化
     */
    private Long submitTime;

    /**
     * 任务开始运行的时间，注意，这里的开始时间是任务真正开始运行的时间，不是提交的时间，因为他可能被堆积在队列中
     */
    private Long startTime;

    /**
     * 任务的结束时间，无奈他是正常结束，或者是异常，这个时间都会存在，当然，被拒绝的任务不在此列！
     */
    private Long endTime;

    /**
     * 任务的执行耗时
     */
    @TableField("runIng_consuming_time")
    private Long runIngConsumingTime;

    /**
     * 任务等待时间
     */
    private Long waitTime;

    /**
     * 任务总耗时
     */
    private Long consumingTime;

    /**
     * 当任务被拒绝策略执行的时候，该值为true,否则为false!
     */
    private boolean refuse;

    /**
     * 任务是否被执行成功，如果中途异常、被拒绝，该值都会被设置为false, 否则为true
     */
    private boolean success;

    /**
     * 任务的异常信息，当没有异常的时候，这个值为空！
     */
    private String throwable;


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

}
