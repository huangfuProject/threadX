package com.threadx.metrics.log4j.config;

/**
 * *************************************************<br/>
 * threadx配置信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:34
 */
public enum ThreadXMetricsLog4jPropertiesEnum {
    /**
     * 使用log输出，指定的日志文件位置
     */
    THREADX_METRICS_OUT_LOG_CONFIG_PATH("threadx.thread.pool.metrics.log4j.config.path", "not set"),
    ;

    /**
     * 配置key
     */
    private final String key;
    /**
     * 默认值
     */
    private final String defaultValue;

    ThreadXMetricsLog4jPropertiesEnum(String key, String defaultValue) {
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
