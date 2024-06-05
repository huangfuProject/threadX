package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Log;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.dto.UserInfoDto;
import com.threadx.metrics.server.dto.UserLoginDto;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.service.UserService;
import com.threadx.metrics.server.vo.LoginUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 用户操作
 *
 * @author huangfukexing
 * @date 2023/6/1 09:38
 */
@RestController
@GlobalResultPackage
@Api(tags = "用户常规操作")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Log(value = LogEnum.USER_LOGIN, paramReplace = "password=******")
    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public LoginUserVo login(@RequestBody UserLoginDto userLoginDto) {

        return userService.login(userLoginDto);
    }


    @Login
    @Log(value = LogEnum.USER_LOGOUT)
    @ApiOperation(value = "用户登出")
    @GetMapping("logout")
    public void logout() {
        userService.logout();
    }
}
