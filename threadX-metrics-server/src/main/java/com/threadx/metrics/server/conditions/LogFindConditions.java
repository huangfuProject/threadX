package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 日志的查询条件
 *
 * @author huangfukexing
 * @date 2023/9/18 15:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "日志的查询条件", value = "日志的查询条件")
public class LogFindConditions implements Serializable {
    private static final long serialVersionUID = -583939072965877979L;

    /**
     * 活动key
     */
    @ApiModelProperty(name = "activeKey", value = "活动key")
    private String activeKey;

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
     * 每一页显示的条数
     */
    @ApiModelProperty(name = "pageSize", value = "每一页显示的条数")
    private Integer pageSize = 10;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "pageNumber", value = "当前页码")
    private Integer pageNumber = 1;

}
