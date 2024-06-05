package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Log;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.conditions.UserPageConditions;
import com.threadx.metrics.server.dto.UserInfoDto;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.UserManagerService;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.UserRoleVo;
import com.threadx.metrics.server.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * *************************************************<br/>
 * 用户管理操作<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:56
 */
@RestController
@GlobalResultPackage
@Api(tags = "用户管理操作")
@RequestMapping("/manager/user")
public class UserManagerController {

    private final UserManagerService userManagerService;

    public UserManagerController(UserManagerService userManagerService) {
        this.userManagerService = userManagerService;
    }

    @Login
    @UserPermission(PermissionValue.FIND_USER_DESC)
    @ApiOperation(value = "查询所有的用户详细信息")
    @GetMapping("findUserDesc")
    public UserRoleVo findUserDesc(@RequestParam("userId") Long userId) {
        return userManagerService.findUserDesc(userId);
    }

    @Login
    @UserPermission(PermissionValue.FIND_ALL_USER)
    @ApiOperation(value = "查询所有的用户信息")
    @PostMapping("getAllUser")
    public ThreadxPage<UserVo> findAllUser(@RequestBody UserPageConditions userPageConditions){
        return userManagerService.findAllUser(userPageConditions);
    }

    /**
     * 冻结用户
     * @param userId 用户的id
     */
    @Login
    @Log(value = LogEnum.MANAGER_FREEZE_USER)
    @UserPermission(PermissionValue.USER_DISABLE)
    @ApiOperation(value = "冻结用户")
    @GetMapping("freezeUser")
    public void freezeUser(@RequestParam("userId") Long userId) {
        userManagerService.freezeUser(userId);
    }

    /**
     * 解除冻结用户
     * @param userId 用户的id
     */
    @Login
    @Log(value = LogEnum.MANAGER_ENABLE_USER)
    @UserPermission(PermissionValue.USER_ENABLE)
    @ApiOperation(value = "解封用户")
    @GetMapping("unsealUser")
    public void unsealUser(@RequestParam("userId") Long userId) {
        userManagerService.unsealUser(userId);
    }

    /**
     * 强制删除用户 包含删除所有的依赖关系以及操作日志
     * @param userId 用户的id
     */
    @Login
    @Log(value = LogEnum.FORCE_DELETE_USER)
    @UserPermission(PermissionValue.FORCE_DELETE_USER)
    @ApiOperation(value = "强制删除用户 包含删除所有的依赖关系以及操作日志")
    @GetMapping("forceDeleteUser")
    public void forceDeleteUser(@RequestParam("userId") Long userId) {
        userManagerService.forceDeleteUser(userId);
    }

    /**
     * 保存用户信息
     * @param userRoleVo 用户关系
     */
    @Login
    @Log(value = LogEnum.SAVE_USER, paramReplace = {"password=******"})
    @UserPermission(PermissionValue.USER_SAVE)
    @ApiOperation(value = "保存用户 包括修改  新增")
    @PostMapping("saveUser")
    public void saveUser(@RequestBody UserRoleVo userRoleVo){
        userManagerService.saveUserAndRole(userRoleVo);
    }
}
