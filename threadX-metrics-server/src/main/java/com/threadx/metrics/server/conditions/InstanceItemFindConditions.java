package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实例查询条件
 *
 * @author huangfukexing
 * @date 2023/5/30 15:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "实例查询条件", value = "实例查询条件")
public class InstanceItemFindConditions implements Serializable {
    private static final long serialVersionUID = -5374069600413978786L;

    /**
     * 服务名称
     */
    @ApiModelProperty(name = "instanceItemName", value = "实例名称")
    private String instanceItemName;

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
