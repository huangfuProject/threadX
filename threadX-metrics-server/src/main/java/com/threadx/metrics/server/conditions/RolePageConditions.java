package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色分页条件
 *
 * @author huangfukexing
 * @date 2023/7/24 14:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色查询条件", value = "角色查询条件")
public class RolePageConditions implements Serializable {
    private static final long serialVersionUID = -368949913915022838L;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "roleName", value = "角色名称")
    private String roleName;


    /**
     * 每一页显示的条数
     */
    @ApiModelProperty(name = "pageSize", value = "每一页显示的条数")
    private Integer pageSize = 20;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "currentPage", value = "当前页码")
    private Integer currentPage = 1;
}
