package com.threadx.metrics.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.conditions.ThreadPoolDetailConditions;
import com.threadx.metrics.server.conditions.ThreadPoolPageDataConditions;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.mapper.ThreadPoolDataMapper;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ThreadPoolDataService;
import com.threadx.metrics.server.service.ThreadTaskDataService;
import com.threadx.metrics.server.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * *************************************************<br/>
 * 线程池数据操作业务类<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/23 15:09
 */
@Slf4j
@Service
@SuppressWarnings("all")
@Transactional(rollbackFor = Exception.class)
public class ThreadPoolDataServiceImpl extends ServiceImpl<ThreadPoolDataMapper, ThreadPoolData> implements ThreadPoolDataService {

    @Autowired
    private ThreadTaskDataService threadTaskDataService;

    @Autowired
    private InstanceItemService instanceItemService;

    @Value("${threadx.thread.pool.timeout}")
    private Long threadPoolTimeOut;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void batchSave(Collection<ThreadPoolData> collection) {
        this.saveBatch(collection);
    }

    @Override
    public ThreadxPage<ThreadPoolDataVo> findPageByThreadPoolPageDataConditions(ThreadPoolPageDataConditions threadPoolPageDataConditions) {
        if (threadPoolPageDataConditions == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        ThreadxPage<ThreadPoolDataVo> threadPoolDataVoThreadxPage = new ThreadxPage<>();

        Long updateTime = threadPoolPageDataConditions.getUpdateTime();
        String threadGroupName = threadPoolPageDataConditions.getThreadGroupName();
        Long instanceId = threadPoolPageDataConditions.getInstanceId();

        Integer pageSize = threadPoolPageDataConditions.getPageSize();
        Integer pageNumber = threadPoolPageDataConditions.getPageNumber();

        QueryWrapper<ThreadPoolData> queryThreadPoolId = new QueryWrapper<>();

        queryThreadPoolId.eq(StrUtil.isNotBlank(threadGroupName), "thread_pool_group_name", threadGroupName)
                .eq(instanceId != null, "instance_id", instanceId)
                .gt(updateTime != null && updateTime > 0, "update_time", updateTime)
                .groupBy("thread_pool_name")
                .select("MAX(id) as id");

        List<ThreadPoolData> threadPoolDataIds = baseMapper.selectList(queryThreadPoolId);
        List<Long> threadPoolDataIdList = threadPoolDataIds.stream().map(ThreadPoolData::getId).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(threadPoolDataIdList)) {
            QueryWrapper<ThreadPoolData> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", threadPoolDataIdList);

            Page<ThreadPoolData> page = Page.of(pageNumber, pageSize);
            //分页查询
            Page<ThreadPoolData> threadPoolDataPage = baseMapper.selectPage(page, queryWrapper);


            List<ThreadPoolData> records = threadPoolDataPage.getRecords();
            //线程池的数据映射
            List<ThreadPoolDataVo> threadPoolDataVos = records.stream().map(record -> {
                ThreadPoolDataVo threadPoolDataVo = new ThreadPoolDataVo();
                BeanUtil.copyProperties(record, threadPoolDataVo);
                Integer activeCount = record.getActiveCount();
                threadPoolDataVo.setState(activeCount > 0 ? ThreadPoolDataVo.ACTION_NAME : ThreadPoolDataVo.IDEA_NAME);
                return threadPoolDataVo;
            }).collect(Collectors.toList());

            threadPoolDataVoThreadxPage.setData(threadPoolDataVos);
            threadPoolDataVoThreadxPage.setTotal(threadPoolDataPage.getTotal());
        }else {
            threadPoolDataVoThreadxPage.setData(new ArrayList<>());
            threadPoolDataVoThreadxPage.setTotal(0);
        }

        return threadPoolDataVoThreadxPage;
    }

    @Override
    public ThreadPoolDetailsVo findThreadPoolDetail(ThreadPoolDetailConditions threadPoolDetailConditions) {
        //先查询最新的线程池的详情信息
        String threadPoolName = threadPoolDetailConditions.getThreadPoolName();
        Long instanceId = threadPoolDetailConditions.getInstanceId();
        Long threadPoolDataId = threadPoolDetailConditions.getThreadPoolDataId();

        if(threadPoolDataId == null) {
            if (instanceId== null || StrUtil.isBlank(threadPoolName)) {
                throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
            }
        }
        ThreadPoolData threadPoolData;
        if(threadPoolDataId != null) {
            QueryWrapper<ThreadPoolData> threadPoolDataQueryWrapper = new QueryWrapper<>();
            threadPoolDataQueryWrapper.eq("id", threadPoolDataId);
            threadPoolData = baseMapper.selectOne(threadPoolDataQueryWrapper);
        }else {
            threadPoolData = baseMapper.findByMaxIdAndThreadPoolNameAndInstanceId(threadPoolName, instanceId);
        }
        ThreadPoolDetailsVo threadPoolDetailsVo = buildThreadPoolDetail(threadPoolData);
        ThreadTaskAvgVo avgByInstanceIdAndThreadPoolName = threadTaskDataService.findAvgByInstanceIdAndThreadPoolName(threadPoolName, instanceId);
        ThreadTaskRateVo rateByInstanceIdAndThreadPoolName = threadTaskDataService.findRateByInstanceIdAndThreadPoolName(threadPoolName, instanceId);
        threadPoolDetailsVo.setAverageTimeConsuming(avgByInstanceIdAndThreadPoolName.getAverageTimeConsuming());
        threadPoolDetailsVo.setAverageWaitTimeConsuming(avgByInstanceIdAndThreadPoolName.getAverageWaitTimeConsuming());
        threadPoolDetailsVo.setRefuseRate(rateByInstanceIdAndThreadPoolName.getRefuseRate() + "%");
        threadPoolDetailsVo.setSuccessRate(rateByInstanceIdAndThreadPoolName.getSuccessRate() + "%");


        //对查看的数据进行计数
        UserVo userData = LoginContext.getUserData();
        Long userId = userData.getId();
        String clickCountCacheKey = String.format(RedisCacheKey.USER_CLICK_INSTANCE_COUNT, userId);
        //对点击的实例进行累加，以方便计算top10
        redisTemplate.opsForZSet().incrementScore(clickCountCacheKey, instanceId + "", 1);
        redisTemplate.expire(clickCountCacheKey, 7, TimeUnit.DAYS);

        return threadPoolDetailsVo;
    }

    private ThreadPoolDetailsVo buildThreadPoolDetail(ThreadPoolData threadPoolData) {
        String serverKey = threadPoolData.getServerKey();
        String instanceKey = threadPoolData.getInstanceKey();
        ThreadPoolDetailsVo threadPoolDetailsVo = new ThreadPoolDetailsVo();
        threadPoolDetailsVo.setThreadPoolName(threadPoolData.getThreadPoolName());
        threadPoolDetailsVo.setActiveCount(threadPoolData.getActiveCount());
        threadPoolDetailsVo.setThreadPoolGroupName(threadPoolData.getThreadPoolGroupName());
        threadPoolDetailsVo.setCollectAddress(threadPoolData.getAddress());
        threadPoolDetailsVo.setCompletedCount(threadPoolData.getCompletedTaskCount());
        threadPoolDetailsVo.setCoreSize(threadPoolData.getCorePoolSize());
        threadPoolDetailsVo.setMaxSize(threadPoolData.getMaximumPoolSize());
        threadPoolDetailsVo.setQueueType(threadPoolData.getQueueType());
        threadPoolDetailsVo.setRefuseCount(threadPoolData.getRejectedCount());
        threadPoolDetailsVo.setRefuseType(threadPoolData.getRejectedType());
        threadPoolDetailsVo.setFreeTime(threadPoolData.getKeepAliveTime());
        threadPoolDetailsVo.setTaskTotalCount(threadPoolData.getTaskCount());
        threadPoolDetailsVo.setSurviveThreadCount(threadPoolData.getThisThreadCount());
        threadPoolDetailsVo.setHistoryMaxThreadCount(threadPoolData.getLargestPoolSize());
        threadPoolDetailsVo.setInstanceId(threadPoolData.getInstanceId());

        threadPoolDetailsVo.setInstanceName(instanceKey);

        threadPoolDetailsVo.setServerName(serverKey);
        Long createTime = threadPoolData.getCreateTime();
        if((System.currentTimeMillis() - createTime) < TimeUnit.SECONDS.toMillis(threadPoolTimeOut)) {
            threadPoolDetailsVo.setState("执行任务");
        }else {
            threadPoolDetailsVo.setState("等待任务");
        }
        if (!instanceItemService.instanceActiveCheck(serverKey, instanceKey)) {
            threadPoolDetailsVo.setState("断连");
            threadPoolDetailsVo.setSurviveThreadCount(0);
        }

        return threadPoolDetailsVo;
    }
}
