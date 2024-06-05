package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.code.RoleExceptionCode;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.common.exceptions.RoleException;
import com.threadx.metrics.server.conditions.RolePageConditions;
import com.threadx.metrics.server.conditions.RoleUserConditions;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.entity.*;
import com.threadx.metrics.server.mapper.RoleMapper;
import com.threadx.metrics.server.service.*;
import com.threadx.metrics.server.vo.RoleAuthorityVo;
import com.threadx.metrics.server.vo.RoleVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 *
 * @author huangfukexing
 * @date 2023/7/24 14:01
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMenuService roleMenuService;
    private final RolePermissionService rolePermissionService;
    private final MenuService menuService;
    private final PermissionService permissionService;
    private final UserRoleService userRoleService;
    private final StringRedisTemplate redisTemplate;
    private final UserManagerService userManagerService;

    public RoleServiceImpl(RoleMenuService roleMenuService, RolePermissionService rolePermissionService, MenuService menuService, PermissionService permissionService, UserRoleService userRoleService, StringRedisTemplate redisTemplate, UserManagerService userManagerService) {
        this.roleMenuService = roleMenuService;
        this.rolePermissionService = rolePermissionService;
        this.menuService = menuService;
        this.permissionService = permissionService;
        this.userRoleService = userRoleService;
        this.redisTemplate = redisTemplate;
        this.userManagerService = userManagerService;
    }

    @Override
    public ThreadxPage<RoleVo> findAllByPage(RolePageConditions rolePageConditions) {
        Integer pageSize = rolePageConditions.getPageSize();
        Integer currentPage = rolePageConditions.getCurrentPage();
        String roleName = rolePageConditions.getRoleName();
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(roleName), "role_name", roleName);
        Page<Role> page = new Page<>(currentPage, pageSize);
        baseMapper.selectPage(page, queryWrapper);
        //数据转换
        List<RoleVo> roleVoList = page.getRecords().stream().map(record -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleId(record.getId());
            roleVo.setRoleName(record.getRoleName());
            roleVo.setRoleDesc(record.getRoleDesc());
            roleVo.setCreateDate(DateUtil.format(new Date(record.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            roleVo.setUpdateDate(DateUtil.format(new Date(record.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"));
            return roleVo;
        }).collect(Collectors.toList());

        ThreadxPage<RoleVo> voThreadPage = new ThreadxPage<>();
        voThreadPage.setTotal(page.getTotal());
        voThreadPage.setData(roleVoList);
        return voThreadPage;
    }

    @Override
    public RoleAuthorityVo findRoleAuthority(Long roleId) {
        //查询角色的具体信息
        Role role = baseMapper.selectById(roleId);
        if (role == null) {
            throw new GeneralException(CurrencyRequestEnum.DATA_EXCEPTION);
        }
        //查询角色对应的菜单
        Set<Long> menuIds = roleMenuService.findByRoleId(roleId);
        Set<Long> permissionIds = rolePermissionService.findByRoleId(roleId);
        RoleAuthorityVo roleAuthorityVo = new RoleAuthorityVo();
        roleAuthorityVo.setRoleDesc(role.getRoleDesc());
        roleAuthorityVo.setRoleName(role.getRoleName());
        roleAuthorityVo.setMenuIds(menuIds);
        roleAuthorityVo.setPermissionIds(permissionIds);
        roleAuthorityVo.setRoleId(roleId);
        return roleAuthorityVo;
    }

    @Override
    public void saveOrUpdate(RoleAuthorityVo roleAuthorityVo) {
        if (roleAuthorityVo == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Long roleId = roleAuthorityVo.getRoleId();
        if (roleId == null) {
            ((RoleService) AopContext.currentProxy()).saveRole(roleAuthorityVo);
        } else {
            ((RoleService) AopContext.currentProxy()).updateRole(roleAuthorityVo);
        }
    }

    @Override
    public void saveRole(RoleAuthorityVo roleAuthorityVo) {
        String roleName = roleAuthorityVo.getRoleName();
        String roleDesc = roleAuthorityVo.getRoleDesc();
        if (StrUtil.isBlank(roleName) || StrUtil.isBlank(roleDesc)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //查询角色名是否重名
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name", roleName);
        List<Role> roles = baseMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(roles)) {
            throw new RoleException(RoleExceptionCode.NAME_DUPLICATION);
        }
        //开始创建角色
        Role role = new Role();
        role.init();
        role.setRoleName(roleName);
        role.setRoleDesc(roleDesc);
        save(role);
        //开始创建菜单映射
        Set<Long> menuIds = roleAuthorityVo.getMenuIds();
        if (CollUtil.isNotEmpty(menuIds)) {
            Set<RoleMenu> roleMenus = menuIds.stream().map(menuId -> {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(role.getId());
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toSet());

            //插入菜单映射
            roleMenuService.batchSave(roleMenus);
        }
        //开始创建权限映射
        Set<Long> permissionIds = roleAuthorityVo.getPermissionIds();
        if (CollUtil.isNotEmpty(permissionIds)) {
            Set<RolePermission> rolePermissions = permissionIds.stream().map(permissionId -> {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                return rolePermission;
            }).collect(Collectors.toSet());
            rolePermissionService.batchSave(rolePermissions);
        }
    }

    @Override
    public void updateRole(RoleAuthorityVo roleAuthorityVo) {
        if (roleAuthorityVo == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        Long roleId = roleAuthorityVo.getRoleId();
        if (roleId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //查询角色信息
        Role role = baseMapper.selectById(roleId);
        if (role == null) {
            throw new RoleException(RoleExceptionCode.ROLE_IN_EXISTENCE);
        }

        String roleNameNew = roleAuthorityVo.getRoleName();
        String roleDescNew = roleAuthorityVo.getRoleDesc();

        if (StrUtil.isBlank(roleNameNew) || StrUtil.isBlank(roleDescNew)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Role newRole = new Role();
        newRole.setId(role.getId());
        newRole.setCreateTime(role.getCreateTime());
        newRole.setUpdateTime(System.currentTimeMillis());
        newRole.setRoleName(roleNameNew);
        newRole.setRoleDesc(roleDescNew);
        //删除旧角色数据
        ((RoleService) AopContext.currentProxy()).simpleDeleteById(roleId);
        //插入新数据
        baseMapper.insert(newRole);


        Set<Long> menuIds = roleAuthorityVo.getMenuIds();
        Set<Long> permissionIds = roleAuthorityVo.getPermissionIds();
        //查询当前的菜单是否存在，将不存在的过滤掉
        List<Menu> menuServiceByIds = menuService.findByIds(menuIds);
        //筛选出id
        Set<RoleMenu> roleMenuNew = menuServiceByIds.stream().map(menu -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menu.getId());
            return roleMenu;
        }).collect(Collectors.toSet());
        //将原有的映射删除
        roleMenuService.deleteByRoleId(roleId);
        //将映射重新入库
        roleMenuService.batchSave(roleMenuNew);

        //查询当前的菜单是否存在，将不存在的过滤掉
        List<Permission> permissions = permissionService.findByIds(permissionIds);
        //转换数据
        Set<RolePermission> newRolePermission = permissions.stream().map(permission -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permission.getId());
            return rolePermission;
        }).collect(Collectors.toSet());
        //将原有的映射删除
        rolePermissionService.deleteByRoleId(roleId);
        //将映射重新入库
        rolePermissionService.batchSave(newRolePermission);
        //查询当前角色下关联的用户
        List<Long> userIds = userRoleService.findUserIdByRoleId(roleId);
        //删除对应用户的权限缓存信息
        for (Long userId : userIds) {
            String permissionCacheKey = String.format(RedisCacheKey.USER_PERMISSION_CACHE, userId);
            redisTemplate.delete(permissionCacheKey);
        }
    }

    @Override
    public void simpleDeleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void deleteRoleById(Long roleId) {
        //查询当前角色关联的用户id
        List<Long> userIds = userRoleService.findUserIdByRoleId(roleId);
        //删除角色 - 菜单映射
        roleMenuService.deleteByRoleId(roleId);
        //删除角色 - 权限映射
        rolePermissionService.deleteByRoleId(roleId);
        //删除用户 - 角色映射
        userRoleService.deleteByRoleId(roleId);
        //删除角色信息
        ((RoleService) AopContext.currentProxy()).simpleDeleteById(roleId);
        //删除用户登录缓存
        for (Long userId : userIds) {
            //清除当前用户的缓存信息
            Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, userId) + "*");
            if (CollUtil.isNotEmpty(keys)) {
                keys.forEach(redisTemplate::delete);
            }
        }
    }

    @Override
    public void untieUserRole(Long roleId, Long userId) {
        userRoleService.untieUserRole(roleId, userId);
    }

    @Override
    public ThreadxPage<UserVo> findRoleUser(RoleUserConditions roleUserConditions) {
        ThreadxPage<UserVo> userVoThreadPage = new ThreadxPage<>();
        if (roleUserConditions == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Long roleId = roleUserConditions.getRoleId();
        if (roleId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //查询用户的id
        ThreadxPage<UserRole> userRole = userRoleService.findPageUserIdByRoleId(roleUserConditions);
        List<UserRole> userRoleData = userRole.getData();


        if (CollUtil.isEmpty(userRoleData)) {
            userVoThreadPage.setData(new ArrayList<>());
            userVoThreadPage.setTotal(0);
            return userVoThreadPage;
        }

        Set<Long> userIdSet = userRoleData.stream().map(UserRole::getUserId).collect(Collectors.toSet());
        //查询对应的用户信息
        List<User> users = userManagerService.findByUserIds(userIdSet);
        List<UserVo> userVos = users.stream().map(user -> {
            UserVo userVo = new UserVo();
            userVo.setId(user.getId());
            userVo.setUserName(user.getUserName());
            userVo.setNickName(user.getNickName());
            userVo.setEmail(user.getEmail());
            userVo.setState(user.getState());
            userVo.setCreateTime(DateUtil.format(new Date(user.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            userVo.setUpdateTime(DateUtil.format(new Date(user.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"));
            return userVo;
        }).collect(Collectors.toList());


        userVoThreadPage.setTotal(userRole.getTotal());
        userVoThreadPage.setData(userVos);
        return userVoThreadPage;
    }

    @Override
    public List<RoleVo> findAllRole() {
        List<Role> roles = baseMapper.selectList(new QueryWrapper<>());
        return roles.stream().map(role -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setRoleId(role.getId());
            roleVo.setRoleDesc(role.getRoleDesc());
            roleVo.setRoleName(role.getRoleName());
            roleVo.setCreateDate(DateUtil.format(new Date(role.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            roleVo.setUpdateDate(DateUtil.format(new Date(role.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"));
            return roleVo;
        }).collect(Collectors.toList());
    }
}
