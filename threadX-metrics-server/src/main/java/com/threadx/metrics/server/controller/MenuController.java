package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.entity.Menu;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.MenuService;
import com.threadx.metrics.server.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author huangfukexing
 * @date 2023/6/1 15:32
 */
@RestController
@GlobalResultPackage
@Api(tags = "菜单信息")
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @Login
    @ApiOperation(value = "查询当前用户的菜单信息")
    @PostMapping("findThisUserMenu")
    public List<Menu> findThisUserMenu(){
        return menuService.findThisUserMenu();
    }


    @Login
    @ApiOperation(value = "查询所有的菜单信息")
    @UserPermission(PermissionValue.FIND_ALL_MENU_LIST)
    @GetMapping("findAllMenu")
    public List<MenuVo> findAllMenu(){
        return menuService.findAllMenu();
    }
}
