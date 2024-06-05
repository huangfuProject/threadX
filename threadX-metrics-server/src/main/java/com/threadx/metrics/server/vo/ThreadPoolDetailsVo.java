package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程池详情数据<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/7/1 19:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程池详情数据", value = "线程池详情数据")
public class ThreadPoolDetailsVo implements Serializable {
    private static final long serialVersionUID = 6990263879570131649L;

    /**
     * 实例的Id
     */
    @ApiModelProperty(name = "instanceId", value = "实例的Id")
    private Long instanceId;

    /**
     * 所属服务的名
     */
    @ApiModelProperty(name = "serverName", value = "所属服务的名")
    private String serverName;

    /**
     * 所属实例的名称
     */
    @ApiModelProperty(name = "instanceName", value = "所属实例的名称")
    private String instanceName;

    /**
     * 当前线程池的状态
     */
    @ApiModelProperty(name = "state", value = "当前线程池的状态")
    private String state;

    /**
     * 拒绝比
     */
    @ApiModelProperty(name = "refuseRate", value = "拒绝比")
    private String refuseRate;

    /**
     * 成功率
     */
    @ApiModelProperty(name = "successRate", value = "成功率")
    private String successRate;

    /**
     * 执行平均耗时
     */
    @ApiModelProperty(name = "averageTimeConsuming", value = "执行平均耗时")
    private String averageTimeConsuming;

    /**
     * 平均等待耗时
     */
    @ApiModelProperty(name = "averageWaitTimeConsuming", value = "平均等待耗时")
    private String averageWaitTimeConsuming;

    /**
     * 线程池组的名称
     */
    @ApiModelProperty(name = "threadPoolGroupName", value = "线程池组的名称")
    private String threadPoolGroupName;

    /**
     * 线程池的名称
     */
    @ApiModelProperty(name = "threadPoolName", value = "线程池的名称")
    private String threadPoolName;

    /**
     * 核心树
     */
    @ApiModelProperty(name = "coreSize", value = "核心数量")
    private Integer coreSize;

    /**
     * 最大执行数
     */
    @ApiModelProperty(name = "maxSize", value = "最大执行数")
    private Integer maxSize;

    /**
     * 活跃数量
     */
    @ApiModelProperty(name = "activeCount", value = "活跃数量")
    private Integer activeCount;

    /**
     * 存活线程数
     */
    @ApiModelProperty(name = "surviveThreadCount", value = "存活线程数")
    private Integer surviveThreadCount;

    /**
     * 历史最大线程数
     */
    @ApiModelProperty(name = "historyMaxThreadCount", value = "历史最大线程数")
    private Integer historyMaxThreadCount;

    /**
     * 拒绝数量
     */
    @ApiModelProperty(name = "refuseCount", value = "拒绝数量")
    private Long refuseCount;

    /**
     * 总提交过的任务总数
     */
    @ApiModelProperty(name = "taskTotalCount", value = "总提交过的任务总数，当前线程池从本次监控开始后，堆积的、执行中的、已经完成的任务的总和")
    private Long taskTotalCount;

    /**
     * 完成的总数
     */
    @ApiModelProperty(name = "completedCount", value = "完成的总数")
    private Long completedCount;

    /**
     * 设定空闲时间
     */
    @ApiModelProperty(name = "freeTime", value = "设定空闲时间")
    private Long freeTime;

    /**
     * 采集机器
     */
    @ApiModelProperty(name = "collectAddress", value = "采集机器")
    private String collectAddress;

    /**
     * 队列类型
     */
    @ApiModelProperty(name = "queueType", value = "队列类型")
    private String queueType;

    /**
     * 拒绝策略
     */
    @ApiModelProperty(name = "refuseType", value = "拒绝策略")
    private String refuseType;


}
