package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程池详情查询条件<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/7/1 20:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "线程池数据查询", value = "线程池数据查询")
public class ThreadPoolDetailConditions implements Serializable {
    private static final long serialVersionUID = -7007445637309401448L;
    /**
     * 实例的Id
     */
    @ApiModelProperty(name = "instanceId", value = "实例的Id")
    private Long instanceId;

    /**
     * 线程池的id
     */
    @ApiModelProperty(name = "threadPoolDataId", value = "线程池的id")
    private Long threadPoolDataId;

    /**
     * 线程池的对象id
     */
    @ApiModelProperty(name = "objectId", value = "线程池对象内存的id")
    private String objectId;

    /**
     * 线程池的名称
     */
    @ApiModelProperty(name = "threadPoolName", value = "线程池的名称")
    private String threadPoolName;

}
