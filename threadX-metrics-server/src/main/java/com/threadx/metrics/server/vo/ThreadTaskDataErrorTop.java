package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *************************************************<br/>
 * 线程任务执行错误top映射<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/10 22:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程任务执行错误top映射", value = "线程任务执行错误top映射")
public class ThreadTaskDataErrorTop {

    /**
     * 实例信息
     */
    @ApiModelProperty(name = "instanceId", value = "实例信息")
    private Long instanceId;

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
     * 所属实例信息
     */
    @ApiModelProperty(name = "instanceName", value = "所属实例信息")
    private String instanceName;

    /**
     * 错误数量
     */
    @ApiModelProperty(name = "errorCount", value = "错误数量")
    private Long errorCount;
}
