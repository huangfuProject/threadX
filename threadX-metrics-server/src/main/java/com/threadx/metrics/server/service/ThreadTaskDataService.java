package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.ThreadTaskConditions;
import com.threadx.metrics.server.dto.ThreadTaskDataErrorCalculation;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * *************************************************<br/>
 * 线程池任务业务服务<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/23 15:09
 */
public interface ThreadTaskDataService {

    /**
     * 统计计算线程池线程任务错误计算
     *
     * @param limit 查询多少条
     * @return 错误统计
     */
    List<ThreadTaskDataErrorTop> findThreadTaskDataErrorCalculation(int limit);

    /**
     * 根据条件查询
     *
     * @param threadTaskConditions 线程任务条件
     * @return 线程任务
     */
    ThreadxPage<ThreadTaskVo> findByThreadTaskConditions(ThreadTaskConditions threadTaskConditions);


    /**
     * 查询 根据实例id和线程池名称查询对象线程池的 成功率和拒绝率
     * @param threadPoolName 线程池名称
     * @param instanceId 实例的id
     * @return 成功率和拒绝率
     */
    ThreadTaskRateVo findRateByInstanceIdAndThreadPoolName(String threadPoolName, Long instanceId);

    /**
     * 查询 根据实例id和线程池名称查询对象线程池的 成功率和拒绝率
     * @param threadPoolName 线程池名称
     * @param instanceId 实例的id
     * @return 成功率和拒绝率
     */
    ThreadTaskAvgVo findAvgByInstanceIdAndThreadPoolName(String threadPoolName, Long instanceId);


    /**
     * 批量保存
     *
     * @param collection 需要批量保存的
     */
    void batchSave(Collection<ThreadTaskData> collection);
}
