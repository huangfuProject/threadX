package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.conditions.LogFindConditions;
import com.threadx.metrics.server.dto.UserDto;
import com.threadx.metrics.server.entity.ActiveLog;
import com.threadx.metrics.server.entity.Permission;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.mapper.ActiveLogMapper;
import com.threadx.metrics.server.service.ActiveLogService;
import com.threadx.metrics.server.service.PermissionService;
import com.threadx.metrics.server.service.RolePermissionService;
import com.threadx.metrics.server.vo.ThreadxPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * *************************************************<br/>
 * <br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ActiveLogServiceImpl extends ServiceImpl<ActiveLogMapper, ActiveLog> implements ActiveLogService {

    private final RolePermissionService rolePermissionService;
    private final PermissionService permissionService;

    public ActiveLogServiceImpl(RolePermissionService rolePermissionService, PermissionService permissionService) {
        this.rolePermissionService = rolePermissionService;
        this.permissionService = permissionService;
    }

    @Override
    public ThreadxPage<ActiveLog> findActiveLogByLogFindConditions(LogFindConditions logFindConditions) {
        String activeKey = logFindConditions.getActiveKey();
        Integer pageNumber = logFindConditions.getPageNumber();
        Integer pageSize = logFindConditions.getPageSize();
        String startTime = logFindConditions.getStartTime();
        String endTime = logFindConditions.getEndTime();

        //获取当前登陆用用户
        UserDto userData = LoginContext.getUserData();
        //验证当前用户角色是否存在查询他人线程池操作日志的权限
        //获取用户所有的权限信息
        List<Long> roleIds = userData.getRoleIds();
        Set<Long> permissionIds = rolePermissionService.findByRoleIds(roleIds);
        // 获取所有的权限信息
        List<Permission> permissionServiceByIds = permissionService.findByIds(permissionIds);


        QueryWrapper<ActiveLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(activeKey), "active_key", activeKey);
        if (StrUtil.isNotBlank(startTime)) {
            long time = DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss").getTime();
            queryWrapper.ge("create_time", time);
        }
        if (StrUtil.isNotBlank(endTime)) {
            long time = DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss").getTime();
            queryWrapper.le("create_time", time);
        }

        boolean hasPermission = false;
        //验证是否存在查询所有日志的权限
        for (Permission permissionServiceById : permissionServiceByIds) {
            String permissionKey = permissionServiceById.getPermissionKey();
            if (PermissionValue.FIND_ALL_LOG.getPermissionKey().equals(permissionKey)) {
                hasPermission = true;
                break;
            }
        }
        // 不存在权限信息 则查询自己的  存在就查询全部的
        if (!hasPermission) {
            queryWrapper.eq("user_id", userData.getId());
        }

        queryWrapper.orderByDesc("create_time");

        Page<ActiveLog> logPage = new Page<>(pageNumber, pageSize);
        baseMapper.selectPage(logPage, queryWrapper);
        ThreadxPage<ActiveLog> activeLogThreadPage = new ThreadxPage<>();
        activeLogThreadPage.setTotal(logPage.getTotal());
        activeLogThreadPage.setData(logPage.getRecords());
        return activeLogThreadPage;
    }

    @Override
    public void saveLog(ActiveLog activeLog) {
        if (activeLog != null) {
            save(activeLog);
        }
    }

    @Override
    public void batchSave(List<ActiveLog> activeLogs) {
        if (CollUtil.isNotEmpty(activeLogs)) {
            saveBatch(activeLogs);
        }
    }

    @Override
    public void deleteLogByUserId(Long userId) {
        if (userId == null) {
            return;
        }
        QueryWrapper<ActiveLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        baseMapper.delete(queryWrapper);
    }
}
