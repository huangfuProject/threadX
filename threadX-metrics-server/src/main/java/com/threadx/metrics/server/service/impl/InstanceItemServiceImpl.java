package com.threadx.metrics.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.conditions.InstanceItemFindConditions;
import com.threadx.metrics.server.constant.InstanceItemState;
import com.threadx.metrics.server.constant.LockName;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.dto.UserDto;
import com.threadx.metrics.server.entity.InstanceItem;
import com.threadx.metrics.server.entity.ServerItem;
import com.threadx.metrics.server.lock.DistributedLockTemplate;
import com.threadx.metrics.server.mapper.InstanceItemMapper;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ServerItemService;
import com.threadx.metrics.server.service.ThreadPoolDataService;
import com.threadx.metrics.server.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * *************************************************<br/>
 * 实例信息实现类<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:31
 */
@Slf4j
@Service
@SuppressWarnings("all")
@Transactional(rollbackFor = Exception.class)
public class InstanceItemServiceImpl extends ServiceImpl<InstanceItemMapper, InstanceItem> implements InstanceItemService {

    @Autowired
    private ServerItemService serverItemService;

    @Autowired
    private ThreadPoolDataService poolDataService;

    @Autowired
    private DistributedLockTemplate distributedLockTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;
    private Cache<String, Long> taskCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build();

    @Value("${threadx.instance.timeout}")
    private Long instanceTimout;

    @Override
    public ThreadxPage<InstanceItemVo> findByPage(InstanceItemFindConditions conditions) {
        if (conditions == null) {
            conditions = new InstanceItemFindConditions();
        }
        Integer pageSize = conditions.getPageSize();
        Integer pageNumber = conditions.getPageNumber();
        if (pageSize == null) {
            pageSize = 6;
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }
        QueryWrapper<InstanceItem> queryWrapper = new QueryWrapper<>();
        //获取实例的名称
        String instanceItemName = conditions.getInstanceItemName();
        queryWrapper.like(StrUtil.isNotBlank(instanceItemName), "instance_name", instanceItemName);
        Page<InstanceItem> page = Page.of(pageNumber, pageSize);
        baseMapper.selectPage(page, queryWrapper);
        //获取分页查询到的数据
        List<InstanceItem> instanceItems = page.getRecords();
        ThreadxPage<InstanceItemVo> threadPage = new ThreadxPage<>();
        if (CollUtil.isNotEmpty(instanceItems)) {
            List<InstanceItemVo> instanceItemVoList = getInstanceItemVos(instanceItems);

            //设置数据
            threadPage.setData(instanceItemVoList);
            // 数据总量
            threadPage.setTotal(page.getTotal());
        }

        return threadPage;
    }

    @Override
    public List<InstanceItem> findInIds(Collection<Long> ids) {
        if (CollUtil.isNotEmpty(ids)) {
            QueryWrapper<InstanceItem> query = Wrappers.query();
            query.in("id", ids);
            return baseMapper.selectList(query);
        }
        return new ArrayList<>();
    }

    @Override
    public List<InstanceItem> findInServerIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        QueryWrapper<InstanceItem> queryInstanceWrapper = new QueryWrapper<>();
        queryInstanceWrapper.in("server_id", ids);
        return baseMapper.selectList(queryInstanceWrapper);
    }

    /**
     * 监测实例是否处于活跃状态   true活跃  false  断连
     *
     * @param instanceId 实例的id
     * @return 实例活跃检查
     */
    @Override
    public boolean instanceActiveCheck(String serverName, String instanceName) {
        Boolean hasKey = redisTemplate.hasKey(String.format(RedisCacheKey.INSTANCE_ACTIVE_CACHE, serverName, instanceName));
        return hasKey != null && hasKey;
    }

    @Override
    public InstanceItem findById(Long instanceId) {
        return baseMapper.selectById(instanceId);
    }

    /**
     * 转换未前端映射对象
     *
     * @param instanceItems 数据库实体对象
     * @return 前端映射对象
     */
    private List<InstanceItemVo> getInstanceItemVos(List<InstanceItem> instanceItems) {

        return instanceItems.stream().map(instanceItem -> {
            InstanceItemVo instanceItemVo = new InstanceItemVo();
            Long instanceId = instanceItem.getId();
            //获取服务名称
            Long serverId = instanceItem.getServerId();
            String instanceName = instanceItem.getInstanceName();
            String serverName = instanceItem.getServerName();
            instanceItemVo.setState(InstanceItemState.NOT_ACTIVE);
            if (instanceActiveCheck(serverName, instanceName)) {
                instanceItemVo.setState(InstanceItemState.ACTIVE);
            }
            instanceItemVo.setId(instanceId);
            instanceItemVo.setServerName(serverName);
            instanceItemVo.setInstanceName(instanceName);
            instanceItemVo.setCreateDate(DateUtil.format(new Date(instanceItem.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            return instanceItemVo;
        }).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public List<InstanceItemVo> commonlyUsedTop10() {
        UserDto userData = LoginContext.getUserData();
        Long userId = userData.getId();
        String clickCountCacheKey = String.format(RedisCacheKey.USER_CLICK_INSTANCE_COUNT, userId);
        // 检查有序集合是否已过期
        boolean has = redisTemplate.hasKey(clickCountCacheKey);
        if (has) {
            RedisScript<List<String>> defaultRedisScript = new DefaultRedisScript(String.format("return redis.call('ZREVRANGE', '%s', 0, 9)", clickCountCacheKey), List.class);
            // 获取前 N 个点击次数最多的记录
            List<String> topRecords = redisTemplate.execute(defaultRedisScript, new ArrayList<>());
            //分割出instanceId
            if (CollUtil.isNotEmpty(topRecords)) {
                //将实例的id进行分割出来
                List<Long> instanceIdList = topRecords.stream().map(e -> Long.parseLong(e)).collect(Collectors.toList());
                //根据实例的id进行查询数据
                QueryWrapper<InstanceItem> queryWrapper = new QueryWrapper<>();
                queryWrapper.in("id", instanceIdList);
                List<InstanceItem> instanceItems = baseMapper.selectList(queryWrapper);
                //对查询出来的数据重新转化未map
                Map<Long, InstanceItem> mapInstanceItem = new HashMap<>(10);
                for (InstanceItem instanceItem : instanceItems) {
                    mapInstanceItem.put(instanceItem.getId(), instanceItem);
                }

                //对结果集进行重新排序，使他与redis取的数据顺序一致
                List<InstanceItem> instanceItemListNew = new ArrayList<>();
                for (Long id : instanceIdList) {
                    InstanceItem instanceItem = mapInstanceItem.get(id);
                    if (instanceItem != null) {
                        instanceItemListNew.add(instanceItem);
                    }
                }

                //转换数据
                return getInstanceItemVos(instanceItemListNew);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public InstanceItem findByInstanceNameAndServerName(String serverName, String instanceName) {
        if (StrUtil.isBlank(serverName) || StrUtil.isBlank(instanceName)) {
            return null;
        }

        ServerItem serverItem = serverItemService.findByName(serverName);
        if (serverItem == null) {
            return null;
        }

        QueryWrapper<InstanceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("server_id", serverItem.getId());
        queryWrapper.eq("instance_name", instanceName);

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public InstanceItem findByInstanceNameAndServerNameOrCreate(String serverName, String instanceName) {
        distributedLockTemplate.lock(LockName.INSTANCE_CREATE_SELECT_LOCK);
        try {
            if (StrUtil.isBlank(serverName) || StrUtil.isBlank(instanceName)) {
                return null;
            }
            InstanceItem instanceItem = ((InstanceItemServiceImpl) AopContext.currentProxy()).findByInstanceNameAndServerName(serverName, instanceName);
            if (instanceItem == null) {
                ServerItem serverItem = serverItemService.findByNameOrCreate(serverName);
                instanceItem = new InstanceItem();
                instanceItem.init();
                instanceItem.setInstanceName(instanceName);
                instanceItem.setActiveTime(instanceItem.getCreateTime());
                instanceItem.setServerId(serverItem.getId());
                instanceItem.setServerName(serverItem.getServerName());
                ((InstanceItemServiceImpl) AopContext.currentProxy()).save(instanceItem);
            }
            return instanceItem;
        } finally {
            distributedLockTemplate.unLock(LockName.INSTANCE_CREATE_SELECT_LOCK);
        }
    }

    @Override
    @SuppressWarnings("all")
    public Long findByInstanceNameAndServerNameOrCreateOnCache(String serverName, String instanceName) {
        //先从一级缓存中获取
        String cacheKey = String.format(RedisCacheKey.INSTANCE_ID_CACHE, serverName, instanceName);
        Long instanceId = taskCache.getIfPresent(cacheKey);
        if (instanceId == null) {
            //一级缓存没有从二级缓存中获取
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            String instanceIdStr = redisTemplate.opsForValue().get(cacheKey);
            if (StrUtil.isNotBlank(instanceIdStr)) {
                instanceId = Long.parseLong(instanceIdStr);

            } else {
                //二级缓存没有就查数据库
                InstanceItem instanceItem = ((InstanceItemServiceImpl) AopContext.currentProxy()).findByInstanceNameAndServerNameOrCreate(serverName, instanceName);
                if (instanceItem == null) {
                    throw new RuntimeException("serverKey or instanceKey is null.");
                }
                instanceId = instanceItem.getId();
            }
            //写入或者更新到二级缓存
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(instanceId), 1, TimeUnit.DAYS);
            //写入到一级缓存
            taskCache.put(cacheKey, instanceId);
        }
        redisTemplate.opsForValue().set(String.format(RedisCacheKey.INSTANCE_ACTIVE_CACHE, serverName, instanceName), "活跃", instanceTimout, TimeUnit.SECONDS);
        return instanceId;
    }

    @Override
    public InstanceItemDataVo instanceListeningState(Long instanceId) {

        if (instanceId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //首先根据ID查询实例信息
        InstanceItem instanceItem = getById(instanceId);
        if (instanceItem == null) {
            log.error("No queried is instance data, instanceId = {}", instanceId);
            throw new GeneralException(CurrencyRequestEnum.DATA_EXCEPTION);
        }
        //查询服务
        Long serverId = instanceItem.getServerId();
        ServerItem serverItem = serverItemService.selectById(serverId);
        if (serverItem == null) {
            log.error("No queried is server data, serverId = {}", serverId);
            throw new GeneralException(CurrencyRequestEnum.DATA_EXCEPTION);
        }

        InstanceItemDataVo instanceItemDataVo = new InstanceItemDataVo();
        //所属服务
        instanceItemDataVo.setServerName(serverItem.getServerName());
        Long createTime = instanceItem.getCreateTime();
        String formatBetween = DateUtil.formatBetween(System.currentTimeMillis() - createTime);
        //监控时间
        instanceItemDataVo.setMonitoringDuration(formatBetween);
        InstanceStateCountVo threadPoolStateCountByInstanceId = poolDataService.findThreadPoolStateCountByInstanceId(instanceId);
        instanceItemDataVo.setServerName(serverItem.getServerName());
        instanceItemDataVo.setThreadPoolCount(threadPoolStateCountByInstanceId.getTotalCount());
        instanceItemDataVo.setActiveThreadPoolCount(threadPoolStateCountByInstanceId.getActiveCount());
        instanceItemDataVo.setWaitThreadPoolCount(threadPoolStateCountByInstanceId.getWaitCount());
        //对查看的数据进行计数
        UserDto userData = LoginContext.getUserData();
        Long userId = userData.getId();
        String clickCountCacheKey = String.format(RedisCacheKey.USER_CLICK_INSTANCE_COUNT, userId);
        //对点击的实例进行累加，以方便计算top10
        redisTemplate.opsForZSet().incrementScore(clickCountCacheKey, instanceId + "", 1);
        redisTemplate.expire(clickCountCacheKey, 7, TimeUnit.DAYS);

        return instanceItemDataVo;
    }
}
