package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.entity.RoleMenu;
import com.threadx.metrics.server.mapper.RoleMenuMapper;
import com.threadx.metrics.server.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 菜单  服务实现
 *
 * @author huangfukexing
 * @date 2023/7/24 10:42
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Override
    public Set<Long> findByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return new HashSet<>();
        }
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("role_id", roleIds);
        return baseMapper.selectList(queryWrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
    }

    @Override
    public void batchSave(Collection<RoleMenu> roleMenus) {
        if(CollUtil.isNotEmpty(roleMenus)) {
            super.saveBatch(roleMenus, 100);
        }

    }

    @Override
    public Set<Long> findByRoleId(Long roleId) {
        if(roleId == null) {
            return null;
        }
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        return baseMapper.selectList(queryWrapper).stream().map(RoleMenu::getMenuId).collect(Collectors.toSet());
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        if(roleId == null) {
            return;
        }
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        baseMapper.delete(queryWrapper);
    }
}
