package com.threadx.metrics.tms.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * *************************************************<br/>
 * threadx配置信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:34
 */
@AllArgsConstructor
@Getter
public enum ThreadXMetricsTmsPropertiesEnum {
    /**
     * 使用log输出，指定的日志文件位置
     */
    THREADX_METRICS_OUT_TMS_HOST("threadx.thread.pool.metrics.tms.host", "not set"),
    THREADX_METRICS_OUT_TMS_PORT("threadx.thread.pool.metrics.tms.port", "not set"),
    ;

    /**
     * 配置key
     */
    private final String key;
    /**
     * 默认值
     */
    private final String defaultValue;
}
