package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.RolePageConditions;
import com.threadx.metrics.server.conditions.RoleUserConditions;
import com.threadx.metrics.server.entity.Role;
import com.threadx.metrics.server.vo.RoleAuthorityVo;
import com.threadx.metrics.server.vo.RoleVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.UserVo;

import java.util.List;

/**
 * 角色服务
 *
 * @author huangfukexing
 * @date 2023/7/24 14:01
 */
public interface RoleService {

    /**
     * 查询所有的角色分页
     *
     * @param rolePageConditions 角色查询条件
     * @return 分页数据
     */
    ThreadxPage<RoleVo> findAllByPage(RolePageConditions rolePageConditions);

    /**
     * 获取角色的具体权限
     *
     * @param roleId 角色的id
     * @return 角色的具体权限信息
     */
    RoleAuthorityVo findRoleAuthority(Long roleId);

    /**
     * 保存角色信息
     *
     * @param roleAuthorityVo 角色权限
     */
    void saveOrUpdate(RoleAuthorityVo roleAuthorityVo);

    /**
     * 保存角色信息
     *
     * @param roleAuthorityVo 角色信息
     */
    void saveRole(RoleAuthorityVo roleAuthorityVo);

    /**
     * 修改角色
     *
     * @param roleAuthorityVo 角色权限
     */
    void updateRole(RoleAuthorityVo roleAuthorityVo);

    /**
     * 仅仅删除角色  不删除角色关联的数据  以及操作角色关联的用户相关信息
     *
     * @param roleId 角色的id
     */
    void simpleDeleteById(Long roleId);

    /**
     * 删除角色信息  连带和角色有关的所有资料
     * 1. 删除  角色 - 菜单映射
     * 2. 删除  角色 - 权限映射
     * 3. 删除  用户 - 角色映射
     * 4. 删除  角色信息
     * 5. 下线与当前角色有关的所有用户（删除登录缓存）
     *
     * @param roleId 角色的id
     */
    void deleteRoleById(Long roleId);

    /**
     * 解绑用户角色
     *
     * @param roleId 操作的角色
     * @param userId 要解绑的用户
     */
    void untieUserRole(Long roleId, Long userId);

    /**
     * 查询角色下绑定的用户
     *
     * @param roleUserConditions 角色用户查询条件
     * @return 用户信息
     */
    ThreadxPage<UserVo> findRoleUser(RoleUserConditions roleUserConditions);

    /**
     * 查询全部的角色数据
     *
     * @return 全部的角色数据
     */
    List<RoleVo> findAllRole();

}
