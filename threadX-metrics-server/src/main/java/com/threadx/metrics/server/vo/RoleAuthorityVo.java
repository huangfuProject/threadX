package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 角色权限映射
 *
 * @author huangfukexing
 * @date 2023/7/25 11:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "角色权限映射", value = "步骤Vo")
public class RoleAuthorityVo implements Serializable {
    private static final long serialVersionUID = 7197601883771076373L;

    /**
     * 该角色拥有的菜单id
     */
    @ApiModelProperty(name = "menuIds", value = "该角色拥有的菜单id")
    private Set<Long> menuIds;

    /**
     * 该角色拥有的权限id
     */
    @ApiModelProperty(name = "permissionIds", value = "该角色拥有的权限id")
    private Set<Long> permissionIds;

    /**
     * 该角色的名称
     */
    @ApiModelProperty(name = "roleName", value = "该角色的名称")
    private String roleName;

    /**
     * 该角色的介绍
     */
    @ApiModelProperty(name = "roleDesc", value = "该角色的介绍")
    private String roleDesc;

    /**
     * 角色的Id
     */
    @ApiModelProperty(name = "roleId", value = "角色的Id")
    private Long roleId;
}
