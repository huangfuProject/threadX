package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色菜单关联表
 *
 * @author huangfukexing
 * @date 2023/7/24 10:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("role_menu")
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = 1476621277724732281L;

    /**
     * 角色的id
     */
    private Long roleId;

    /**
     * 菜单的id
     */
    private Long menuId;
}
