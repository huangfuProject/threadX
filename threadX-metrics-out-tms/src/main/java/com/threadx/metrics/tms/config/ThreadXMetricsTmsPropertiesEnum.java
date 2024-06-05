package com.threadx.metrics.tms.config;

/**
 * *************************************************<br/>
 * threadx配置信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:34
 */
public enum ThreadXMetricsTmsPropertiesEnum {
    /**
     * tms服务端的地址  ,  分隔
     */
    THREADX_METRICS_OUT_TMS_ADDRESS("threadx.thread.pool.metrics.tms.address", "not set"),
    ;

    /**
     * 配置key
     */
    private final String key;
    /**
     * 默认值
     */
    private final String defaultValue;

    ThreadXMetricsTmsPropertiesEnum(String key, String defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
