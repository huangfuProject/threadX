package com.threadx.metrics.server.service;

import com.threadx.metrics.server.entity.Role;
import com.threadx.metrics.server.entity.RoleMenu;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 角色 菜单权限
 *
 * @author huangfukexing
 * @date 2023/7/24 10:40
 */
public interface RoleMenuService {

    /**
     * 查询所有的菜单id  根据角色的id
     *
     * @param roleIds 角色的id
     * @return 当前角色下 所有的菜单的id
     */
    Set<Long> findByRoleIds(List<Long> roleIds);


    /**
     * 批量保存
     *
     * @param roleMenus 需要批量保存的菜单
     */
    void batchSave(Collection<RoleMenu> roleMenus);


    /**
     * 查询所有的菜单id  根据角色的id
     *
     * @param roleId 角色的id
     * @return 当前角色下 所有的菜单的id
     */
    Set<Long> findByRoleId(Long roleId);

    /**
     * 根据角色的id删除菜单映射
     * @param roleId 角色的id
     */
    void deleteByRoleId(Long roleId);
}
