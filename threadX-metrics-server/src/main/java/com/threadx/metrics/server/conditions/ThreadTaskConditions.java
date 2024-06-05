package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程任务查询条件
 *
 * @author huangfukexing
 * @date 2023/7/5 11:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "线程任务数据查询", value = "线程任务数据查询")
public class ThreadTaskConditions  implements Serializable {
    private static final long serialVersionUID = -8190580573647106413L;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间")
    private String endTime;

    /**
     * 实例的Id
     */
    @ApiModelProperty(name = "instanceId", value = "实例的Id")
    private Long instanceId;
    /**
     * 线程池的名称
     */
    @ApiModelProperty(name = "threadPoolName", value = "线程池的名称")
    private String threadPoolName;


    /**
     * 结果状态
     */
    @ApiModelProperty(name = "resultState", value = "结果状态")
    private String resultState;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "thisPage", value = "当前页码")
    private Integer thisPage = 1;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "pageSizes", value = "每页容量")
    private Integer pageSizes = 10;

    /**
     * 排序字段名
     */
    @ApiModelProperty(name = "sortName", value = "排序字段名")
    private String sortName = "create_time";

    /**
     * 排序类型 0 升序  1降序
     */
    @ApiModelProperty(name = "sortType", value = "排序类型 0 升序  1降序")
    private String sortType = "1";




}
