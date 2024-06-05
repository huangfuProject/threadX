package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.ThreadPoolDetailConditions;
import com.threadx.metrics.server.conditions.ThreadPoolPageDataConditions;
import com.threadx.metrics.server.dto.ThreadPoolVariableParameter;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.entity.ThreadPoolUpdateLog;
import com.threadx.metrics.server.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * *************************************************<br/>
 * 线程池参数业务服务<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/23 15:09
 */
public interface ThreadPoolDataService {

    /**
     * 查找线程池的修改日志
     *
     * @param latelyCount 查询最近几次的
     * @return 最近修改的信息
     */
    List<ThreadPoolUpdateLog> findThreadPoolUpdateLog(int latelyCount);

    /**
     * 查询线程池数据  根据ids
     *
     * @param ids id集合
     * @return 线程池的数据
     */
    List<ThreadPoolData> findThreadPoolDataByIds(Collection<Long> ids);

    /**
     * 批量保存
     *
     * @param collection 需要批量保存的
     */
    void batchSave(Collection<ThreadPoolData> collection);


    /**
     * 根据查询条件分页查询
     *
     * @param threadPoolPageDataConditions 查询条件
     * @return 分页信息
     */
    ThreadxPage<ThreadPoolDataVo> findPageByThreadPoolPageDataConditions(ThreadPoolPageDataConditions threadPoolPageDataConditions);

    /**
     * 查询线程池详情
     *
     * @param threadPoolDetailConditions 实例详情查询
     * @return 实例的详情
     */
    ThreadPoolDetailsVo findThreadPoolDetail(ThreadPoolDetailConditions threadPoolDetailConditions);

    /**
     * 查询线程池状态的数量  根据实例的id
     *
     * @param instanceId 实例的id
     * @return 返回线程池的数量
     */
    InstanceStateCountVo findThreadPoolStateCountByInstanceId(Long instanceId);

    /**
     * 线程池的批量修改活新增
     *
     * @param threadPoolDataList 线程池的批量修改或者新增
     */
    void upsertBatchSavePoolData(List<ThreadPoolData> threadPoolDataList);

    /**
     * 查询线程池的核心参数
     *
     * @param threadPoolDataId 线程池的id
     * @return 对应线程池的核心参数
     */
    ThreadPoolVariableParameter findThreadPoolParam(Long threadPoolDataId);

    /**
     * 修改线程池参数
     *
     * @param threadPoolVariableParameter 线程池参数
     */
    void updateThreadPoolParam(ThreadPoolVariableParameter threadPoolVariableParameter);


    /**
     * 监测线程池是否处于活跃状态   true活跃  false  断连
     *
     * @param serverName     服务名称
     * @param instanceName   实例名称
     * @param threadPoolName 线程池的名称
     * @return 实例活跃检查
     */
    boolean threadPoolActiveCheck(String serverName, String instanceName, String threadPoolName);
}
