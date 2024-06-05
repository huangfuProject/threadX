package com.threadx.metrics.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.conditions.RoleUserConditions;
import com.threadx.metrics.server.entity.UserRole;
import com.threadx.metrics.server.mapper.UserRoleMapper;
import com.threadx.metrics.server.service.UserRoleService;
import com.threadx.metrics.server.vo.ThreadxPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户-角色服务类
 *
 * @author huangfukexing
 * @date 2023/7/24 10:30
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Override
    public List<Long> findRoleIdByUserId(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<UserRole> userRoles = baseMapper.selectList(queryWrapper);
        return userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findUserIdByRoleId(Long roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<UserRole> userRoles = baseMapper.selectList(queryWrapper);
        return userRoles.stream().map(UserRole::getUserId).collect(Collectors.toList());
    }

    @Override
    public ThreadxPage<UserRole> findPageUserIdByRoleId(RoleUserConditions roleUserConditions) {
        if(roleUserConditions == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Long roleId = roleUserConditions.getRoleId();
        if(roleId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Integer pageSize = roleUserConditions.getPageSize();
        if(pageSize == null) {
            pageSize = 20;
        }

        Integer currentPage = roleUserConditions.getCurrentPage();
        if(currentPage == null) {
            currentPage = 1;
        }
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);

        Page<UserRole> userRolePage = new Page<>(currentPage, pageSize);
        baseMapper.selectPage(userRolePage, queryWrapper);
        ThreadxPage<UserRole> userRoleThreadPage = new ThreadxPage<>();
        List<UserRole> records = userRolePage.getRecords();

        userRoleThreadPage.setData(records);
        userRoleThreadPage.setTotal(userRolePage.getTotal());

        return userRoleThreadPage;
    }

    @Override
    public void deleteByUserId(Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public void untieUserRole(Long roleId, Long userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        queryWrapper.eq("user_id", userId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public void batchSave(Collection<UserRole> userRoles) {
        saveBatch(userRoles);
    }
}
