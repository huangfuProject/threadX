package com.threadx.metrics.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.LoginExceptionCode;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.LoginException;
import com.threadx.metrics.server.common.utils.ThreadxJwtUtil;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.constant.UserConstant;
import com.threadx.metrics.server.dto.UserLoginDto;
import com.threadx.metrics.server.entity.User;
import com.threadx.metrics.server.mapper.UserMapper;
import com.threadx.metrics.server.service.MenuService;
import com.threadx.metrics.server.service.PermissionService;
import com.threadx.metrics.server.service.UserRoleService;
import com.threadx.metrics.server.service.UserService;
import com.threadx.metrics.server.vo.LoginUserVo;
import com.threadx.metrics.server.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户实现
 *
 * @author huangfukexing
 * @date 2023/6/1 08:13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final StringRedisTemplate redisTemplate;
    private final UserRoleService userRoleService;
    private final MenuService menuService;
    private final PermissionService permissionService;


    public UserServiceImpl(StringRedisTemplate redisTemplate, UserRoleService userRoleService, MenuService menuService, PermissionService permissionService) {
        this.redisTemplate = redisTemplate;
        this.userRoleService = userRoleService;
        this.menuService = menuService;
        this.permissionService = permissionService;
    }

    @Override
    public List<User> findUserByIds(Collection<Long> userIds) {
        if(CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return baseMapper.selectBatchIds(userIds);
    }

    @Override
    @SuppressWarnings("all")
    public LoginUserVo login(UserLoginDto userLoginDto) {
        //根据用户名查询用户信息
        User user = ((UserServiceImpl) AopContext.currentProxy()).findByUserName(userLoginDto.getUserName());
        if (user == null) {
            throw new LoginException(LoginExceptionCode.USER_NAME_OR_PASSWORD_ERROR);
        }
        String state = user.getState();
        if (UserConstant.DISABLE.equals(state)) {
            throw new LoginException(LoginExceptionCode.USER_IS_FROZEN);
        }

        if (BCrypt.checkpw(userLoginDto.getPassword(), user.getPassword())) {
            //生成当前的用户缓存前缀
            //清除当前用户的所有缓存信息  只允许单人登录
            Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, user.getId()) + "*");
            if(CollUtil.isNotEmpty(keys)) {
                keys.forEach(redisTemplate::delete);
            }
            //查询用户角色
            List<Long> roleIds = userRoleService.findRoleIdByUserId(user.getId());
            //生成token
            UserDto userDto = new UserDto();
            BeanUtil.copyProperties(user, userDto);
            userDto.setRoleIds(roleIds);
            LoginContext.setUserData(userDto);

            Long id = user.getId();
            String tokenKey = String.format("%s%s", IdUtil.fastSimpleUUID(), id);
            String cacheKey = String.format(RedisCacheKey.USER_TOKEN_CACHE, id, tokenKey);
            //返回生成的token
            String token = ThreadxJwtUtil.generateToken(userDto);
            //查询菜单信息
            menuService.findThisUserMenu();
            //查询权限信息
            permissionService.findThisUserPermission();
            //缓存令牌
            redisTemplate.opsForValue().set(cacheKey, token, 1, TimeUnit.HOURS);
            //设置数据
            LoginContext.setUserData(userDto);
            LoginUserVo loginUserVo = new LoginUserVo();
            loginUserVo.setNickName(user.getNickName());
            loginUserVo.setToken(tokenKey);
            return loginUserVo;

        } else {
            throw new LoginException(LoginExceptionCode.USER_NAME_OR_PASSWORD_ERROR);
        }
    }

    @Override
    public User findByUserName(String userName) {
        if (StrUtil.isBlank(userName)) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    @SuppressWarnings("all")
    public void logout() {
        UserDto userData = LoginContext.getUserData();
        if (userData != null) {
            Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, userData.getId()) + "*");
            if (CollUtil.isNotEmpty(keys)) {
                keys.forEach(redisTemplate::delete);
            }
        }


    }

}
