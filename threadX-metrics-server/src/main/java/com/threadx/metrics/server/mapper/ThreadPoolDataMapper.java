package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.vo.InstanceStateCountVo;
import com.threadx.metrics.server.vo.ThreadStatusVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************<br/>
 * 线程池数据mapper<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/23 15:07
 */
@Repository
public interface ThreadPoolDataMapper extends BaseMapper<ThreadPoolData> {


    /**
     * 根据条件查询id最大的那条记录
     *
     * @param threadPoolName 线程池的名称
     * @param instanceId     实例的id
     * @return 线程池的数据
     */
    ThreadPoolData findByMaxIdAndThreadPoolNameAndInstanceId(@Param("threadPoolName") String threadPoolName, @Param("instanceId") Long instanceId);

    /**
     * 查询实例的线程池的状态信息  总数量   活跃的线程池  等待的线程池
     *
     * @param instanceId 实例的信息
     * @return 当前实例的转台信息
     */
    List<ThreadStatusVo> findThreadPoolStateCountByInstanceId(@Param("instanceId") Long instanceId);

    /**
     * 线程池的批量修改活新增
     *
     * @param threadPoolDataList 线程池的批量修改或者新增
     */
    void upsertBatchSavePoolData(@Param("threadPoolDataList") List<ThreadPoolData> threadPoolDataList);

}
