package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.RoleUserConditions;
import com.threadx.metrics.server.entity.UserRole;
import com.threadx.metrics.server.vo.ThreadxPage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户角色服务
 *
 * @author huangfukexing
 * @date 2023/7/24 10:28
 */
public interface UserRoleService {

    /**
     * 根据当前用户的id 查询指定用户下的所有的角色id
     *
     * @param userId 要查询的用户的id
     * @return 用户id
     */
    List<Long> findRoleIdByUserId(Long userId);

    /**
     * 根据角色的id 查询指定用户下的所有的角色id
     *
     * @param roleId 要查询的角色的id
     * @return 用户的id
     */
    List<Long> findUserIdByRoleId(Long roleId);

    /**
     * 根据角色的id 分页查询关联信息
     *
     * @param roleUserConditions 查询条件
     * @return 分页数据
     */
    ThreadxPage<UserRole> findPageUserIdByRoleId(RoleUserConditions roleUserConditions);

    /**
     * 根据传递的用户信息 删除用户下的角色信息
     *
     * @param userId 用户的id
     */
    void deleteByUserId(Long userId);

    /**
     * 根据角色id删除用户的映射关系
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(Long roleId);

    /**
     * 解绑用户角色
     *
     * @param roleId 操作的角色
     * @param userId 要解绑的用户
     */
    void untieUserRole(Long roleId, Long userId);

    /**
     * 批量插入数据
     * @param userRoles 用户角色映射
     */
    void batchSave(Collection<UserRole> userRoles);
}
