package com.threadx.metrics.server.service;

import com.threadx.metrics.server.entity.RolePermission;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色权限服务
 *
 * @author huangfukexing
 * @date 2023/7/24 10:40
 */
public interface RolePermissionService {

    /**
     * 查询所有的权限id  根据角色的id
     *
     * @param roleIds 角色的id
     * @return 当前角色下 所有的权限的id
     */
    Set<Long> findByRoleIds(List<Long> roleIds);

    /**
     * 查询所有的权限id  根据角色的id
     *
     * @param roleId 角色的id
     * @return 当前角色下 所有的权限的id
     */
    Set<Long> findByRoleId(Long roleId);

    /**
     * 批量保存
     *
     * @param rolePermissions 角色 权限对应信息
     */
    void batchSave(Collection<RolePermission> rolePermissions);

    /**
     * 删除映射信息 根据角色信息
     * @param roleId 角色id
     */
    void deleteByRoleId(Long roleId);
}
