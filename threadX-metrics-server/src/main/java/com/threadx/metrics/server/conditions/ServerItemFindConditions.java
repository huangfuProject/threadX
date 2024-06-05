package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务数据查询条件
 *
 * @author huangfukexing
 * @date 2023/5/10 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "服务分页查询条件", value = "服务查询条件")
public class ServerItemFindConditions implements Serializable {
    private static final long serialVersionUID = 7008461579814903174L;

    /**
     * 服务名称
     */
    @ApiModelProperty(name = "serverItemName", value = "服务名称")
    private String serverItemName;

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
