package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程任务映射<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/7/1 12:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程任务映射", value = "线程任务映射")
public class ThreadTaskVo implements Serializable {
    private static final long serialVersionUID = -4974289796123794269L;

    /**
     * 任务的id
     */
    @ApiModelProperty(name = "id", value = "任务的id")
    private Long id;

    /**
     * 线程的名称
     */
    @ApiModelProperty(name = "parentId", value = "线程的名称")
    private String threadName;

    /**
     * 任务提交时间
     */
    @ApiModelProperty(name = "submitTime", value = "任务提交时间")
    private String submitDate;

    /**
     * 任务开始时间
     */
    @ApiModelProperty(name = "startTime", value = "任务开始时间")
    private String startDate;

    /**
     * 任务结束时间
     */
    @ApiModelProperty(name = "endTime", value = "任务结束时间")
    private String endDate;

    /**
     * 任务执行耗时
     */
    @ApiModelProperty(name = "runIngConsumingTime", value = "任务执行耗时")
    private Long runIngConsumingTime;


    /**
     * 任务等待时间
     */
    @ApiModelProperty(name = "waitTime", value = "任务等待时间")
    private Long waitTime;

    /**
     * 任务总耗时
     */
    @ApiModelProperty(name = "consumingTime", value = "任务总耗时")
    private Long consumingTime;


    /**
     * 当任务被拒绝策略执行的时候，该值为true,否则为false!
     */
    @ApiModelProperty(name = "refuse", value = "当任务被拒绝策略执行的时候，该值为true,否则为false!")
    private boolean refuse;

    /**
     * 任务是否被执行成功，如果中途异常、被拒绝，该值都会被设置为false, 否则为true
     */
    @ApiModelProperty(name = "success", value = "任务是否被执行成功，如果中途异常、被拒绝，该值都会被设置为false, 否则为true")
    private boolean success;

    /**
     * 任务的异常信息，当没有异常的时候，这个值为空！
     */
    @ApiModelProperty(name = "throwable", value = "任务的异常信息，当没有异常的时候，这个值为空！")
    private String throwable;
}
