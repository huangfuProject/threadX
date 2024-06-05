package com.threadx.metrics.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.communication.common.agreement.packet.SyncMessage;
import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateRequestMessage;
import com.threadx.communication.server.ServerSendMessage;
import com.threadx.communication.server.cache.ConnectionCache;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.code.ThreadPoolExceptionCode;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.common.exceptions.ThreadPoolException;
import com.threadx.metrics.server.conditions.LogFindConditions;
import com.threadx.metrics.server.conditions.ThreadPoolDetailConditions;
import com.threadx.metrics.server.conditions.ThreadPoolPageDataConditions;
import com.threadx.metrics.server.constant.LogConstant;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.dto.ThreadPoolVariableParameter;
import com.threadx.metrics.server.dto.UserDto;
import com.threadx.metrics.server.entity.*;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.mapper.ThreadPoolDataMapper;
import com.threadx.metrics.server.service.*;
import com.threadx.metrics.server.vo.*;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    private ThreadPoolDataMapper threadPoolDataMapper;

    @Autowired
    private ActiveLogService activeLogService;

    @Autowired
    private UserService userService;

    @Value("${threadx.thread.pool.timeout}")
    private Long threadPoolTimeOut;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<ThreadPoolUpdateLog> findThreadPoolUpdateLog(int latelyCount) {
        LogFindConditions logFindConditions = new LogFindConditions();
        logFindConditions.setActiveKey(LogEnum.UPDATE_THREAD_PARAM.getActiveKey());
        logFindConditions.setPageSize(latelyCount);
        logFindConditions.setPageNumber(1);
        ThreadxPage<ActiveLog> activeLogByLogFindConditions = activeLogService.findActiveLogByLogFindConditions(logFindConditions);
        List<ActiveLog> datas = activeLogByLogFindConditions.getData();
        Set<Long> userIds = new HashSet<>();
        Set<Long> threadPoolIds = new HashSet<>();
        Map<Long, ThreadPoolVariableParameter> paramMapping = new HashMap<>();
        datas.forEach(data -> {
            String paramData = data.getParamData();
            if (StrUtil.isNotBlank(paramData)) {
                paramData = paramData.substring(paramData.indexOf("=>") + 2, paramData.length());

                ThreadPoolVariableParameter threadPoolVariableParameter = JSONUtil.toBean(paramData, ThreadPoolVariableParameter.class);
                //保存用户信息
                userIds.add(data.getUserId());
                //保存线程池信息
                threadPoolIds.add(threadPoolVariableParameter.getThreadPoolId());
                //保存参数信息
                paramMapping.put(data.getId(), threadPoolVariableParameter);
            }
        });
        //获取所有的用户信息
        List<User> userByIds = userService.findUserByIds(userIds);
        //获取所有的线程池信息
        List<ThreadPoolData> threadPoolDataByIds = this.findThreadPoolDataByIds(threadPoolIds);
        //转换为map
        Map<Long, String> userIdNameMaping = new HashMap<>(userByIds.size());
        Map<Long, ThreadPoolData> threadPoolDataMaping = new HashMap<>(userByIds.size());
        //映射为  用户id  和   用户名称
        userByIds.forEach(user -> userIdNameMaping.put(user.getId(), user.getNickName()));
        //映射为  线程池id  和 线程池信息
        threadPoolDataByIds.forEach(threadPoolData -> threadPoolDataMaping.put(threadPoolData.getId(), threadPoolData));

        //开始转换数据
        return datas.stream().map(data -> {
            ThreadPoolUpdateLog threadPoolUpdateLog = new ThreadPoolUpdateLog();
            threadPoolUpdateLog.setUpdateNickName(userIdNameMaping.getOrDefault(data.getUserId(), "未知用户"));
            //获取参数信息
            ThreadPoolVariableParameter threadPoolVariableParameter = paramMapping.get(data.getId());
            //获取线程数据
            Long threadPoolId = threadPoolVariableParameter.getThreadPoolId();
            ThreadPoolData threadPoolData = threadPoolDataMaping.get(threadPoolId);
            threadPoolUpdateLog.setThreadPoolId(threadPoolId);
            threadPoolUpdateLog.setThreadPoolName(threadPoolData.getThreadPoolName());
            threadPoolUpdateLog.setServerName(threadPoolData.getServerKey());
            threadPoolUpdateLog.setInstanceName(threadPoolData.getInstanceKey());
            threadPoolUpdateLog.setUpdateDate(DateUtil.format(new Date(data.getCreateTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            threadPoolUpdateLog.setInstanceId(threadPoolData.getInstanceId());
            threadPoolUpdateLog.formatUpdateDetails(threadPoolVariableParameter);
            String resultState = data.getResultState();
            threadPoolUpdateLog.setResultMessage(LogConstant.ERROR.equals(resultState) ? "失败" : "成功");
            return threadPoolUpdateLog;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ThreadPoolData> findThreadPoolDataByIds(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public void batchSave(Collection<ThreadPoolData> collection) {
        this.saveBatch(collection);
    }

    @Override
    public ThreadxPage<ThreadPoolDataVo> findPageByThreadPoolPageDataConditions(ThreadPoolPageDataConditions threadPoolPageDataConditions) {
        if (threadPoolPageDataConditions == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Long instanceId = threadPoolPageDataConditions.getInstanceId();
        if (instanceId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        InstanceItem instanceItem = instanceItemService.findById(instanceId);
        if (instanceItem == null) {
            log.error("No queried is instance data, instanceId = {}", instanceId);
            throw new GeneralException(CurrencyRequestEnum.DATA_EXCEPTION);
        }

        ThreadxPage<ThreadPoolDataVo> threadPoolDataVoThreadxPage = new ThreadxPage<>();

        Long updateTime = threadPoolPageDataConditions.getUpdateTime();
        String threadGroupName = threadPoolPageDataConditions.getThreadGroupName();

        Integer pageSize = threadPoolPageDataConditions.getPageSize();
        Integer pageNumber = threadPoolPageDataConditions.getPageNumber();

        QueryWrapper<ThreadPoolData> queryThreadPoolId = new QueryWrapper<>();
        //查询分组下的最大值id
        queryThreadPoolId.like(StrUtil.isNotBlank(threadGroupName), "thread_pool_group_name", threadGroupName)
                .eq(instanceId != null, "instance_id", instanceId)
                .gt(updateTime != null && updateTime > 0, "update_time", updateTime)
                .groupBy("thread_pool_name")
                .select("MAX(id) as id");

        List<ThreadPoolData> threadPoolDataIds = baseMapper.selectList(queryThreadPoolId);
        List<Long> threadPoolDataIdList = threadPoolDataIds.stream().map(ThreadPoolData::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(threadPoolDataIdList)) {
            QueryWrapper<ThreadPoolData> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", threadPoolDataIdList);

            Page<ThreadPoolData> page = Page.of(pageNumber, pageSize);
            //分页查询
            Page<ThreadPoolData> threadPoolDataPage = baseMapper.selectPage(page, queryWrapper);


            List<ThreadPoolData> records = threadPoolDataPage.getRecords();
            //获取当前的实例的状态
            boolean isActive = instanceItemService.instanceActiveCheck(instanceItem.getServerName(), instanceItem.getInstanceName());
            //线程池的数据映射
            List<ThreadPoolDataVo> threadPoolDataVos = records.stream().map(record -> {
                ThreadPoolDataVo threadPoolDataVo = new ThreadPoolDataVo();
                BeanUtil.copyProperties(record, threadPoolDataVo);
                Integer activeCount = record.getActiveCount();
                threadPoolDataVo.setCreateTime(DateUtil.format(new Date(record.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
                threadPoolDataVo.setInstanceId(record.getInstanceId());
                if (!isActive) {
                    threadPoolDataVo.setState(ThreadPoolDataVo.DISCONNECTION);
                    threadPoolDataVo.setActiveCount(0);
                    threadPoolDataVo.setThisThreadCount(0);
                } else {
                    boolean threadPoolActive = threadPoolActiveCheck(record.getServerKey(), record.getInstanceKey(), record.getThreadPoolName());
                    if (threadPoolActive) {
                        threadPoolDataVo.setState(activeCount > 0 ? ThreadPoolDataVo.ACTION_NAME : ThreadPoolDataVo.IDEA_NAME);
                    } else {
                        threadPoolDataVo.setState(ThreadPoolDataVo.DISCONNECTION);
                    }
                }

                List<ProcedureVo> procedureVos = new ArrayList<>();
                //获取创建流
                String threadPoolFlow = record.getThreadPoolFlow();
                //分隔数据
                String[] flowItems = threadPoolFlow.split("->");
                for (int i = 0; i < flowItems.length; i++) {
                    ProcedureVo procedureVo = new ProcedureVo();
                    procedureVo.setTitle("流程" + (i + 1));
                    procedureVo.setDetails(flowItems[i]);
                    procedureVos.add(procedureVo);
                }
                threadPoolDataVo.setCreateThreadPoolFlow(procedureVos);
                threadPoolDataVo.setObjectId(record.getThreadPoolObjectId());
                return threadPoolDataVo;
            }).collect(Collectors.toList());

            threadPoolDataVoThreadxPage.setData(threadPoolDataVos);
            threadPoolDataVoThreadxPage.setTotal(threadPoolDataPage.getTotal());
        } else {
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

        if (threadPoolDataId == null) {
            if (instanceId == null || StrUtil.isBlank(threadPoolName)) {
                throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
            }
        }
        ThreadPoolData threadPoolData;
        if (threadPoolDataId != null) {
            QueryWrapper<ThreadPoolData> threadPoolDataQueryWrapper = new QueryWrapper<>();
            threadPoolDataQueryWrapper.eq("id", threadPoolDataId);
            threadPoolData = baseMapper.selectOne(threadPoolDataQueryWrapper);
        } else {
            threadPoolData = baseMapper.findByMaxIdAndThreadPoolNameAndInstanceId(threadPoolName, instanceId);
        }

        if (threadPoolData == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        ThreadPoolDetailsVo threadPoolDetailsVo = buildThreadPoolDetail(threadPoolData);
        ThreadTaskAvgVo avgByInstanceIdAndThreadPoolName = threadTaskDataService.findAvgByInstanceIdAndThreadPoolName(threadPoolData.getThreadPoolName(), threadPoolData.getInstanceId());
        ThreadTaskRateVo rateByInstanceIdAndThreadPoolName = threadTaskDataService.findRateByInstanceIdAndThreadPoolName(threadPoolData.getThreadPoolName(), threadPoolData.getInstanceId());

        threadPoolDetailsVo.setAverageTimeConsuming(avgByInstanceIdAndThreadPoolName == null ? "0" : avgByInstanceIdAndThreadPoolName.getAverageTimeConsuming());
        threadPoolDetailsVo.setAverageWaitTimeConsuming(avgByInstanceIdAndThreadPoolName == null ? "0" : avgByInstanceIdAndThreadPoolName.getAverageWaitTimeConsuming());
        threadPoolDetailsVo.setRefuseRate(rateByInstanceIdAndThreadPoolName == null ? "0%" : rateByInstanceIdAndThreadPoolName.getRefuseRate() + "%");
        threadPoolDetailsVo.setSuccessRate(rateByInstanceIdAndThreadPoolName == null ? "100%" : rateByInstanceIdAndThreadPoolName.getSuccessRate() + "%");


        //对查看的数据进行计数
        UserDto userData = LoginContext.getUserData();
        Long userId = userData.getId();
        String clickCountCacheKey = String.format(RedisCacheKey.USER_CLICK_INSTANCE_COUNT, userId);
        //对点击的实例进行累加，以方便计算top10
        redisTemplate.opsForZSet().incrementScore(clickCountCacheKey, instanceId + "", 1);
        redisTemplate.expire(clickCountCacheKey, 7, TimeUnit.DAYS);

        return threadPoolDetailsVo;
    }

    @Override
    public InstanceStateCountVo findThreadPoolStateCountByInstanceId(Long instanceId) {
        InstanceStateCountVo instanceStateCountVo = new InstanceStateCountVo();
        List<ThreadStatusVo> threadPoolStateCountByInstanceId = baseMapper.findThreadPoolStateCountByInstanceId(instanceId);

        List<ThreadStatusVo> activeThreadPoolData = threadPoolStateCountByInstanceId.stream().filter(state -> {
            return 1 == state.getHasActive();
        }).collect(Collectors.toList());
        int totalCount = threadPoolStateCountByInstanceId.size();
        int activeCount = activeThreadPoolData.size();
        int failure = totalCount - activeCount;
        instanceStateCountVo.setTotalCount(totalCount);
        instanceStateCountVo.setWaitCount(failure);
        instanceStateCountVo.setActiveCount(activeCount);
        return instanceStateCountVo;
    }

    @Override
    public void upsertBatchSavePoolData(List<ThreadPoolData> threadPoolDataList) {
        threadPoolDataMapper.upsertBatchSavePoolData(threadPoolDataList);
    }

    @Override
    public ThreadPoolVariableParameter findThreadPoolParam(Long threadPoolDataId) {
        //参数验证
        if (threadPoolDataId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //查询线程池
        QueryWrapper<ThreadPoolData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", threadPoolDataId);
        ThreadPoolData threadPoolData = baseMapper.selectOne(queryWrapper);
        if (threadPoolData == null) {
            throw new ThreadPoolException(ThreadPoolExceptionCode.NOT_EXIST_THREAD_POOL_DATA);
        }
        ThreadPoolVariableParameter threadPoolVariableParameter = new ThreadPoolVariableParameter();
        threadPoolVariableParameter.setThreadPoolId(threadPoolData.getId());
        threadPoolVariableParameter.setCoreSize(threadPoolData.getCorePoolSize());
        threadPoolVariableParameter.setMaximumPoolSize(threadPoolData.getMaximumPoolSize());
        threadPoolVariableParameter.setKeepAliveTime(threadPoolData.getKeepAliveTime());
        threadPoolVariableParameter.setRejectedExecutionHandlerClass(threadPoolData.getRejectedType());
        return threadPoolVariableParameter;
    }

    @Override
    public void updateThreadPoolParam(ThreadPoolVariableParameter threadPoolVariableParameter) {
        if (threadPoolVariableParameter == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        Long threadPoolId = threadPoolVariableParameter.getThreadPoolId();
        if (threadPoolId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        //查询对应的线程池
        ThreadPoolData threadPoolData = baseMapper.selectById(threadPoolId);
        if (threadPoolData == null) {
            throw new ThreadPoolException(ThreadPoolExceptionCode.NOT_EXIST_THREAD_POOL_DATA);
        }
        //初步判断线程池是否存活
        String serverKey = threadPoolData.getServerKey();
        String instanceKey = threadPoolData.getInstanceKey();
        String threadPoolName = threadPoolData.getThreadPoolName();
        //实例是否存活
        if (!threadPoolActiveCheck(serverKey, instanceKey, threadPoolName)) {
            throw new ThreadPoolException(ThreadPoolExceptionCode.THREAD_POOL_DISCONNECTION);
        }
        //获取线程池的采集节点地址
        String address = threadPoolData.getAddress();
        //验证是否存活
        if (!ConnectionCache.isActive(address)) {
            throw new ThreadPoolException(ThreadPoolExceptionCode.THREAD_POOL_DISCONNECTION);
        }
        ThreadPoolUpdateRequestMessage threadPoolUpdateRequestMessage = new ThreadPoolUpdateRequestMessage(
                threadPoolData.getThreadPoolObjectId(),
                threadPoolVariableParameter.getCoreSize(), threadPoolVariableParameter.getMaximumPoolSize(),
                threadPoolVariableParameter.getKeepAliveTime(), threadPoolVariableParameter.getRejectedExecutionHandlerClass());
        //发送修改请求
        SyncMessage syncMessage = ServerSendMessage.syncSendMessage(address, threadPoolUpdateRequestMessage);
        if (!syncMessage.isSuccess()) {
            throw new GeneralException(syncMessage.getErrorMessage());
        }
    }

    @Override
    public boolean threadPoolActiveCheck(String serverName, String instanceName, String threadPoolName) {
        Boolean hasKey = redisTemplate.hasKey(String.format(RedisCacheKey.THREAD_ACTIVE_CACHE, serverName, instanceName, threadPoolName));
        return hasKey != null && hasKey;
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
        if ((System.currentTimeMillis() - createTime) < TimeUnit.SECONDS.toMillis(threadPoolTimeOut)) {
            threadPoolDetailsVo.setState("执行任务");
        } else {
            threadPoolDetailsVo.setState("等待任务");
        }
        if (!instanceItemService.instanceActiveCheck(serverKey, instanceKey)) {
            threadPoolDetailsVo.setState("断连");
            threadPoolDetailsVo.setSurviveThreadCount(0);
        }

        return threadPoolDetailsVo;
    }
}
