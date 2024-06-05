package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色映射
 *
 * @author huangfukexing
 * @date 2023/7/24 15:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "步骤Vo", value = "步骤Vo")
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 6097582693550234215L;

    /**
     * 角色的id
     */
    @ApiModelProperty(name = "roleId", value = "角色的id")
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(name = "roleName", value = "角色名称")
    private String roleName;

    /**
     * 角色介绍
     */
    @ApiModelProperty(name = "roleDesc", value = "角色介绍")
    private String roleDesc;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createDate", value = "创建时间")
    private String createDate;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "updateDate", value = "修改时间")
    private String updateDate;
}
