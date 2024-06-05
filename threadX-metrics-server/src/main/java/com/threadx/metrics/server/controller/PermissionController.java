package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.PermissionService;
import com.threadx.metrics.server.vo.PermissionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限控制器
 *
 * @author huangfukexing
 * @date 2023/7/24 17:00
 */
@RestController
@GlobalResultPackage
@Api(tags = "权限信息")
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Login
    @GetMapping("findAllPermission")
    @UserPermission(PermissionValue.FIND_ALL_PERMISSION_LIST)
    @ApiOperation(value = "查询所有的权限信息")
    public List<PermissionVo> findAllPermission(){
        return permissionService.findAllPermission();
    }
}
