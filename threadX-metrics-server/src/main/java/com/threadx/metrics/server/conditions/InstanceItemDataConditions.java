package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 实例数据查询条件<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/5 11:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "实例详情查询条件", value = "实例详情查询条件")
public class InstanceItemDataConditions implements Serializable {
    private static final long serialVersionUID = 16312390746073682L;
    /**
     * 实例的id
     */
    @ApiModelProperty(name = "instanceId", value = "实例的id")
    private Long instanceId;
}
