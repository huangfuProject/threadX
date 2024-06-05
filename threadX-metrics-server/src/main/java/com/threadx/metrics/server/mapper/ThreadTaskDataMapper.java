package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.vo.ThreadTaskAvgVo;
import com.threadx.metrics.server.vo.ThreadTaskDataErrorTop;
import com.threadx.metrics.server.vo.ThreadTaskRateVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * *************************************************<br/>
 * 线程池任务数据mapper<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/23 15:07
 */
@Repository
public interface ThreadTaskDataMapper extends BaseMapper<ThreadTaskData> {

    /**
     * 统计计算线程池线程任务错误计算
     * @param limit 查询多少条
     * @return 错误统计
     */
    List<ThreadTaskDataErrorTop> findThreadTaskDataErrorCalculation(@Param("limit") int limit);

    /**
     * 查询 根据实例id和线程池名称查询对象线程池的 成功率和拒绝率
     * @param threadPoolName 线程池名称
     * @param instanceId 实例的id
     * @return 成功率和拒绝率
     */
    ThreadTaskRateVo findRateByInstanceIdAndThreadPoolName(@Param("threadPoolName")String threadPoolName, @Param("instanceId")Long instanceId);

    /**
     * 查询 根据实例id和线程池名称查询对象线程池的 成功率和拒绝率
     * @param threadPoolName 线程池名称
     * @param instanceId 实例的id
     * @return 成功率和拒绝率
     */
    ThreadTaskAvgVo findAvgByInstanceIdAndThreadPoolName(@Param("threadPoolName")String threadPoolName, @Param("instanceId")Long instanceId);
}
