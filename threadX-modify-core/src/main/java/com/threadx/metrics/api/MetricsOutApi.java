package com.threadx.metrics.api;

import com.threadx.metrics.ThreadPoolExecutorData;
import com.threadx.metrics.ThreadTaskExecutorData;

import java.util.List;

/**
 * 指标数据输出API
 *
 * @author huangfukexing
 * @date 2023/3/24 20:33
 */
public interface MetricsOutApi {

    /**
     * 初始化
     */
    void init();

    /**
     * 输出单条指标数据
     *
     * @param metricsData 指标数据
     */
    void outThreadPoolMetricsData(ThreadPoolExecutorData metricsData);


    /**
     * 输出多条指标数据
     *
     * @param metricsDataList 指标数据列表
     */
    default void outThreadPoolMetricsData(List<ThreadPoolExecutorData> metricsDataList) {
        if (metricsDataList != null && metricsDataList.size() > 0) {
            for (ThreadPoolExecutorData threadPoolExecutorData : metricsDataList) {
                outThreadPoolMetricsData(threadPoolExecutorData);
            }
        }
    }


    /**
     * 输出单条指标数据
     *
     * @param metricsData 指标数据
     */
    void outThreadTaskMetricsData(ThreadTaskExecutorData metricsData);


    /**
     * 输出多条指标数据
     *
     * @param metricsDataList 指标数据列表
     */
    default void outThreadTaskMetricsData(List<ThreadTaskExecutorData> metricsDataList) {
        if (metricsDataList != null && metricsDataList.size() > 0) {
            for (ThreadTaskExecutorData threadTaskExecutorData : metricsDataList) {
                outThreadTaskMetricsData(threadTaskExecutorData);
            }
        }
    }


    /**
     * 销毁对象
     */
    void destroy();

    /**
     * 获取指标名称
     *
     * @return 返回指标名称
     */
    String getMetricsName();
}
