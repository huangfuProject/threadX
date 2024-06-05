package com.threadx.metrics.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限信息表
 *
 * @author huangfukexing
 * @date 2023/7/24 10:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("role_permission")
public class RolePermission implements Serializable {
    private static final long serialVersionUID = 2971534182302785395L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;
}
