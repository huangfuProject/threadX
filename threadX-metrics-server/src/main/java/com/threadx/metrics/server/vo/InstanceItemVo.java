package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 实例数据的映射对象
 *
 * @author huangfukexing
 * @date 2023/5/30 15:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "实例数据的映射对象", value = "实例数据的映射对象")
public class InstanceItemVo {

    /**
     * 当前数据的id
     */
    @ApiModelProperty(name = "id", value = "当前数据的id")
    private Long id;

    /**
     * 当前实例的名称
     */
    @ApiModelProperty(name = "instanceName", value = "当前实例的名称")
    private String instanceName;

    /**
     * 实例所属服务的名称
     */
    @ApiModelProperty(name = "serverName", value = "实例所属服务的名称")
    private String serverName;

    /**
     * 当前实例的创建时间
     */
    @ApiModelProperty(name = "createDate", value = "当前实例的创建时间")
    private String createDate;

    /**
     * 当前实例的状态
     */
    @ApiModelProperty(name = "state", value = "当前实例的状态  0 存活  1断联")
    private String state;
}
