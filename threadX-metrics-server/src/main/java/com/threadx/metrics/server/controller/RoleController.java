package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Log;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.conditions.RolePageConditions;
import com.threadx.metrics.server.conditions.RoleUserConditions;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.RoleService;
import com.threadx.metrics.server.vo.RoleAuthorityVo;
import com.threadx.metrics.server.vo.RoleVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author huangfukexing
 * @date 2023/7/24 13:56
 */
@RestController
@GlobalResultPackage
@Api(tags = "角色信息")
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @Login
    @PostMapping("findAllByPage")
    @UserPermission(PermissionValue.FIND_ALL_ROLE_LIST)
    @ApiOperation(value = "分页查询所有的角色信息")
    public ThreadxPage<RoleVo> findAllByPage(@RequestBody RolePageConditions rolePageConditions) {
        return roleService.findAllByPage(rolePageConditions);
    }

    @Login
    @GetMapping("findAllRole")
    @UserPermission(PermissionValue.FIND_ALL_ROLE_LIST)
    @ApiOperation(value = "查询所有的角色信息")
    public List<RoleVo> findAllRole() {
        return roleService.findAllRole();
    }

    @Login
    @GetMapping("findRoleAuthority")
    @UserPermission(PermissionValue.FIND_ROLE_AUTHORITY)
    @ApiOperation(value = "查询一个角色的全部权限信息")
    public RoleAuthorityVo findRoleAuthority(@RequestParam("roleId") Long roleId) {
        return roleService.findRoleAuthority(roleId);
    }

    @Login
    @PostMapping("save")
    @UserPermission(PermissionValue.SAVE_ROLE_AUTHORITY)
    @Log(value = LogEnum.CREATE_ROLE)
    @ApiOperation(value = "保存角色信息")
    public void save(@RequestBody RoleAuthorityVo roleAuthorityVo) {
        roleService.saveOrUpdate(roleAuthorityVo);
    }

    @Login
    @GetMapping("deleteRole")
    @UserPermission(PermissionValue.DELETE_ROLE_AUTHORITY)
    @Log(value = LogEnum.DELETE_ROLE)
    @ApiOperation(value = "删除角色数据")
    public void deleteRole(@RequestParam("roleId") Long roleId) {
        roleService.deleteRoleById(roleId);
    }

    @Login
    @GetMapping("untieUserRole")
    @UserPermission(PermissionValue.DELETE_ROLE_AUTHORITY)
    @Log(value = LogEnum.UNTIE_USER_ROLE)
    @ApiOperation(value = "解绑用户与角色")
    public void untieUserRole(@RequestParam("roleId") Long roleId, @RequestParam("userId") Long userId) {
        roleService.untieUserRole(roleId, userId);
    }

    @Login
    @PostMapping("findRoleUser")
    @UserPermission(PermissionValue.FIND_ROLE_USER)
    @ApiOperation(value = "查询角色用户")
    public ThreadxPage<UserVo> findRoleUser(@RequestBody RoleUserConditions roleUserConditions) {
        return roleService.findRoleUser(roleUserConditions);
    }
}
