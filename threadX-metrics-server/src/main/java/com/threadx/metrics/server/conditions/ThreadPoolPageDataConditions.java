package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 线程池数据查询条件<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/5 11:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "线程池数据查询", value = "线程池数据查询")
public class ThreadPoolPageDataConditions implements Serializable {
    private static final long serialVersionUID = -2307887771037938309L;

    /**
     * 所属实例
     */
    @ApiModelProperty(name = "instanceId", value = "所属实例")
    private Long instanceId;

    /**
     * 根据线程池组查询
     */
    @ApiModelProperty(name = "threadGroupName", value = "根据线程池组查询")
    private String threadGroupName;

    /**
     * 根据修改时间查询
     */
    @ApiModelProperty(name = "updateTime", value = "根据修改时间查询")
    private Long updateTime;

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
