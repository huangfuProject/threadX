package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.LoginExceptionCode;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.LoginException;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.entity.Menu;
import com.threadx.metrics.server.entity.Permission;
import com.threadx.metrics.server.mapper.PermissionMapper;
import com.threadx.metrics.server.service.PermissionService;
import com.threadx.metrics.server.service.RolePermissionService;
import com.threadx.metrics.server.dto.UserDto;
import com.threadx.metrics.server.vo.MenuVo;
import com.threadx.metrics.server.vo.PermissionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 权限实现
 *
 * @author huangfukexing
 * @date 2023/6/1 14:43
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final StringRedisTemplate redisTemplate;
    private final RolePermissionService rolePermissionService;

    public PermissionServiceImpl(StringRedisTemplate redisTemplate, RolePermissionService rolePermissionService) {
        this.redisTemplate = redisTemplate;
        this.rolePermissionService = rolePermissionService;
    }

    @Override
    public List<Permission> findThisUserPermission() {
        UserDto userData = LoginContext.getUserData();
        if(userData == null) {
            throw new LoginException(LoginExceptionCode.USER_NOT_LOGIN_ERROR);
        }
        Long userId = userData.getId();
        //先查询缓存
        String permissionCacheKey = String.format(RedisCacheKey.USER_PERMISSION_CACHE, userId);
        String permissionData = redisTemplate.opsForValue().get(permissionCacheKey);
        List<Permission> permissions = new ArrayList<>();
        if(StrUtil.isNotBlank(permissionData)) {
            permissions = JSONUtil.toList(permissionData, Permission.class);
            //续期
            redisTemplate.expire(permissionCacheKey, 1, TimeUnit.HOURS);
        }else {
            //查询中间表
            Set<Long> permissionIds = rolePermissionService.findByRoleIds(userData.getRoleIds());
            if(CollUtil.isNotEmpty(permissionIds)) {
                QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id", permissionIds);
                permissions = baseMapper.selectList(queryWrapper);
                if(CollUtil.isNotEmpty(permissions)) {
                    redisTemplate.opsForValue().set(permissionCacheKey, JSONUtil.toJsonStr(permissions), 1, TimeUnit.HOURS);
                }
            }
        }

        return permissions;
    }

    @Override
    public List<PermissionVo> findAllPermission() {
        if (redisTemplate.hasKey(RedisCacheKey.PERMISSION_ALL_CACHE)) {
            //先查询缓存
            String permissionAllStr = redisTemplate.opsForValue().get(RedisCacheKey.PERMISSION_ALL_CACHE);
            //格式化数据
            List<PermissionVo> permissionVo = JSONUtil.toList(permissionAllStr, PermissionVo.class);
            //数据续约
            redisTemplate.expire(RedisCacheKey.PERMISSION_ALL_CACHE, 1, TimeUnit.DAYS);
            return permissionVo;
        }else {
            //查询所有的菜单数据
            List<Permission> permissions = baseMapper.selectList(new QueryWrapper<>());
            //菜单转换
            if (CollUtil.isNotEmpty(permissions)) {
                List<PermissionVo> permissionVos = permissions.stream().map(permission -> {
                    PermissionVo permissionVo = new PermissionVo();
                    permissionVo.setId(permission.getId());
                    permissionVo.setName(permission.getPermissionName());
                    permissionVo.setPermissionDesc(permission.getPermissionDesc());
                    return permissionVo;
                }).collect(Collectors.toList());

                redisTemplate.opsForValue().set(RedisCacheKey.PERMISSION_ALL_CACHE, JSONUtil.toJsonStr(permissionVos), 1, TimeUnit.DAYS);
                return permissionVos;
            }
            return null;

        }
    }

    @Override
    public List<Permission> findByIds(Set<Long> permissionIds) {
        if(CollUtil.isEmpty(permissionIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", permissionIds);
        return baseMapper.selectList(queryWrapper);
    }
}
