package com.threadx.metrics.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.conditions.ThreadTaskConditions;
import com.threadx.metrics.server.entity.InstanceItem;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.mapper.ThreadTaskDataMapper;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ThreadTaskDataService;
import com.threadx.metrics.server.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class ThreadTaskDataServiceImpl extends ServiceImpl<ThreadTaskDataMapper, ThreadTaskData> implements ThreadTaskDataService {

    @Autowired
    private ThreadTaskDataMapper threadTaskDataMapper;

    @Autowired
    private InstanceItemService instanceItemService;

    @Override
    public List<ThreadTaskDataErrorTop> findThreadTaskDataErrorCalculation(int limit) {
        List<ThreadTaskDataErrorTop> threadTaskDataErrorCalculations = threadTaskDataMapper.findThreadTaskDataErrorCalculation(limit);
        if (CollUtil.isNotEmpty(threadTaskDataErrorCalculations)) {
            List<Long> instanceIdList = threadTaskDataErrorCalculations.stream().map(ThreadTaskDataErrorTop::getInstanceId).collect(Collectors.toList());
            List<InstanceItem> inIds = instanceItemService.findInIds(instanceIdList);
            Map<Long, String> instanceItemIndex = new HashMap<>();
            for (InstanceItem instanceItem : inIds) {
                instanceItemIndex.put(instanceItem.getId(), instanceItem.getInstanceName());
            }
            return threadTaskDataErrorCalculations.stream().map(threadTaskDataErrorCalculation -> {
                ThreadTaskDataErrorTop threadTaskDataErrorTop = new ThreadTaskDataErrorTop();
                BeanUtil.copyProperties(threadTaskDataErrorCalculation, threadTaskDataErrorTop);
                threadTaskDataErrorTop.setInstanceName(instanceItemIndex.getOrDefault(threadTaskDataErrorCalculation.getInstanceId(), "未知服务"));
                return threadTaskDataErrorTop;
            }).collect(Collectors.toList());

        }
        return null;
    }

    @Override
    public ThreadxPage<ThreadTaskVo> findByThreadTaskConditions(ThreadTaskConditions threadTaskConditions) {
        String threadPoolName = threadTaskConditions.getThreadPoolName();
        Long instanceId = threadTaskConditions.getInstanceId();
        if (StrUtil.isBlank(threadPoolName) || instanceId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        Integer pageSizes = threadTaskConditions.getPageSizes();
        Integer thisPage = threadTaskConditions.getThisPage();

        String endTimeStr = threadTaskConditions.getEndTime();
        String startTimeStr = threadTaskConditions.getStartTime();
        String resultState = threadTaskConditions.getResultState();

        String sortName = threadTaskConditions.getSortName();
        String sortType = threadTaskConditions.getSortType();

        QueryWrapper<ThreadTaskData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("thread_pool_name", threadPoolName);
        queryWrapper.eq("instance_id", instanceId);

        if (StrUtil.isNotBlank(startTimeStr)) {
            long startTime = DateUtil.parse(startTimeStr, "yyyy-MM-dd HH:mm:ss").getTime();
            queryWrapper.ge("start_time", startTime);
        }

        if (StrUtil.isNotBlank(endTimeStr)) {
            long endTime = DateUtil.parse(endTimeStr, "yyyy-MM-dd HH:mm:ss").getTime();
            queryWrapper.le("end_time", endTime);
        }

        if (StrUtil.isBlank(sortName) || StrUtil.isBlank(sortType)) {
            sortName = "create_time";
            sortType = "1";
        }

        if ("0".equals(sortType)) {
            queryWrapper.orderByAsc(sortName);
        } else {
            queryWrapper.orderByDesc(sortName);
        }

        queryWrapper.eq(StrUtil.isNotBlank(resultState), "success", resultState);

        Page<ThreadTaskData> page = new Page<>();
        page.setCurrent(thisPage).setSize(pageSizes);
        Page<ThreadTaskData> threadTaskDataPage = baseMapper.selectPage(page, queryWrapper);
        ThreadxPage<ThreadTaskVo> threadTaskDataThreadxPage = new ThreadxPage<>();

        List<ThreadTaskData> records = page.getRecords();
        List<ThreadTaskVo> datas = records.stream().map(record -> {
            ThreadTaskVo threadTaskVo = new ThreadTaskVo();
            threadTaskVo.setId(record.getId());
            String threadName = record.getThreadName();
            if (StrUtil.isBlank(threadName)) {
                threadTaskVo.setThreadName("X【未分配线程】");
            } else {
                threadTaskVo.setThreadName(threadName);
            }

            threadTaskVo.setSubmitDate(DateUtil.format(new Date(record.getSubmitTime()), "yyyy-MM-dd HH:mm:ss"));
            threadTaskVo.setStartDate(DateUtil.format(new Date(record.getStartTime()), "yyyy-MM-dd HH:mm:ss"));
            threadTaskVo.setEndDate(DateUtil.format(new Date(record.getEndTime()), "yyyy-MM-dd HH:mm:ss"));
            threadTaskVo.setRunIngConsumingTime(record.getRunIngConsumingTime());
            threadTaskVo.setWaitTime(record.getWaitTime());
            threadTaskVo.setConsumingTime(record.getConsumingTime());
            threadTaskVo.setSuccess(record.isSuccess());
            threadTaskVo.setRefuse(record.isRefuse());
            threadTaskVo.setThrowable(record.getThrowable());
            if (record.isRefuse()) {
                threadTaskVo.setThrowable("该任务被拒绝执行！");
            }
            return threadTaskVo;
        }).collect(Collectors.toList());


        threadTaskDataThreadxPage.setData(datas);
        threadTaskDataThreadxPage.setTotal(page.getTotal());

        return threadTaskDataThreadxPage;
    }

    @Override
    public ThreadTaskRateVo findRateByInstanceIdAndThreadPoolName(String threadPoolName, Long instanceId) {
        return baseMapper.findRateByInstanceIdAndThreadPoolName(threadPoolName, instanceId);
    }

    @Override
    public ThreadTaskAvgVo findAvgByInstanceIdAndThreadPoolName(String threadPoolName, Long instanceId) {
        return baseMapper.findAvgByInstanceIdAndThreadPoolName(threadPoolName, instanceId);
    }

    @Override
    public void batchSave(Collection<ThreadTaskData> collection) {
        this.saveBatch(collection);
    }
}
