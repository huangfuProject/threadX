package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色用户查询条件
 *
 * @author huangfukexing
 * @date 2023/7/26 16:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色绑定用户查询条件", value = "角色绑定用户查询条件")
public class RoleUserConditions implements Serializable {
    private static final long serialVersionUID = -6696580711436110627L;
    @ApiModelProperty(name = "roleId", value = "角色id")
    private Long roleId;


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
