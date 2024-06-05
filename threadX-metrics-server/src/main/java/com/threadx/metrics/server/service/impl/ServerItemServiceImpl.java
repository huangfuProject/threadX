package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.conditions.ServerItemFindConditions;
import com.threadx.metrics.server.constant.LockName;
import com.threadx.metrics.server.entity.InstanceItem;
import com.threadx.metrics.server.entity.ServerItem;
import com.threadx.metrics.server.lock.DistributedLockTemplate;
import com.threadx.metrics.server.mapper.ServerItemMapper;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ServerItemService;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.TreeDataVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * *************************************************<br/>
 * 服务详细的配置<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ServerItemServiceImpl extends ServiceImpl<ServerItemMapper, ServerItem> implements ServerItemService {

    private final DistributedLockTemplate distributedLockTemplate;

    private final InstanceItemService instanceItemService;

    public ServerItemServiceImpl(DistributedLockTemplate distributedLockTemplate, InstanceItemService instanceItemService) {
        this.distributedLockTemplate = distributedLockTemplate;
        this.instanceItemService = instanceItemService;
    }

    @Override
    public ServerItem selectById(Long id) {
        if (id == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        return super.getById(id);
    }

    @Override
    public List<ServerItem> findServerItem(String serverItemName) {
        QueryWrapper<ServerItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(serverItemName), "server_name", serverItemName);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ServerItem> findServerItemInId(List<Long> serverIds) {
        if (CollUtil.isEmpty(serverIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<ServerItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", serverIds);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ThreadxPage<ServerItem> findServerItemByPage(ServerItemFindConditions serverItemFindConditions) {
        if (serverItemFindConditions == null) {
            serverItemFindConditions = new ServerItemFindConditions();
        }
        Integer pageSize = serverItemFindConditions.getPageSize();
        Integer pageNumber = serverItemFindConditions.getPageNumber();
        if (pageSize == null) {
            pageSize = 20;
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        String serverItemName = serverItemFindConditions.getServerItemName();
        QueryWrapper<ServerItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(serverItemName), "server_name", serverItemName);
        Page<ServerItem> page = Page.of(pageNumber, pageSize);
        baseMapper.selectPage(page, queryWrapper);
        ThreadxPage<ServerItem> threadPage = new ThreadxPage<>();
        threadPage.setData(page.getRecords());
        threadPage.setTotal(page.getTotal());
        return threadPage;
    }

    @Override
    public ServerItem findByName(String serverName) {
        if (StrUtil.isBlank(serverName)) {
            return null;
        }
        QueryWrapper<ServerItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_name", serverName);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public ServerItem findByNameOrCreate(String serverName) {
        distributedLockTemplate.tryLock(LockName.SERVER_CREATE_SELECT_LOCK);
        try {
            ServerItem serverItem = ((ServerItemServiceImpl) AopContext.currentProxy()).findByName(serverName);
            if (serverItem == null) {
                serverItem = new ServerItem();
                serverItem.init();
                serverItem.setServerName(serverName);
                ((ServerItemServiceImpl) AopContext.currentProxy()).save(serverItem);
            }
            return serverItem;
        } finally {
            distributedLockTemplate.unLock(LockName.SERVER_CREATE_SELECT_LOCK);
        }

    }

    @Override
    public List<TreeDataVo> findAllServerAndInstanceData() {
        //查询服务的列表
        QueryWrapper<ServerItem> queryWrapper = new QueryWrapper<>();
        List<ServerItem> serverItems = baseMapper.selectList(queryWrapper);
        //筛选所有的服务的id
        List<Long> serverIds = serverItems.stream().map(ServerItem::getId).collect(Collectors.toList());
        //根据服务的id查询实例
        List<InstanceItem> instanceList = instanceItemService.findInServerIds(serverIds);
        //根据服务的id进行数据分组
        Map<Long, List<InstanceItem>> instanceGroup = instanceList.stream().collect(Collectors.groupingBy(InstanceItem::getServerId));
        //开始映射对象
        return serverItems.stream().map(serverItem -> {
            Long serverId = serverItem.getId();
            //创建一个映射类
            TreeDataVo serverTreeDataVo = new TreeDataVo();
            serverTreeDataVo.setId(serverId);
            serverTreeDataVo.setLabel(serverItem.getServerName());
            //获取对应的服务的实例
            List<InstanceItem> instanceItems = instanceGroup.get(serverId);
            if (CollUtil.isNotEmpty(instanceItems)) {
                List<TreeDataVo> instanceVos = instanceItems.stream().map(instanceItem -> {
                    TreeDataVo instanceTreeDataVo = new TreeDataVo();
                    instanceTreeDataVo.setId(instanceItem.getId());
                    instanceTreeDataVo.setLabel(instanceItem.getInstanceName());
                    instanceTreeDataVo.setParentId(serverId);
                    return instanceTreeDataVo;
                }).collect(Collectors.toList());
                serverTreeDataVo.setChildren(instanceVos);
            }
            return serverTreeDataVo;
        }).collect(Collectors.toList());

    }
}













