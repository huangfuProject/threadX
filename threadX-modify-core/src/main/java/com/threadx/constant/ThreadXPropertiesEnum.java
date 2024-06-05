package com.threadx.constant;

/**
 * *************************************************<br/>
 * threadx配置信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:34
 */
public enum ThreadXPropertiesEnum {

    /**
     * 服务标识
     */
    SERVER_MAKE_NAME("threadx.server.name", "not set", true),

    /**
     * 实例标识  每一个实例都应该是一个唯一的存在
     */
    INSTANCE_MAKE_NAME("threadx.instance.name", "not set", true),

    /**
     * 线程池采集间隔
     */
    THREAD_POOL_COLLECTION_INTERVAL("threadx.thread.pool.collection.interval", "3", false),
    /**
     * 线程池的拦截前缀
     */
    THREAD_POOL_INTERCEPT_PREFIX("threadx.thread.pool.intercept.prefix", "not set", false),

    /**
     * 线程指标的输出配置
     */
    THREADX_METRICS_OUT_MODEL("threadx.thread.pool.metrics.model", "log4j", false),
    ;

    /**
     * 配置key
     */
    private final String key;
    /**
     * 默认值
     */
    private final String defaultValue;

    /**
     * 是否是必填项
     */
    private final boolean required;

    ThreadXPropertiesEnum(String key, String defaultValue, boolean required) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isRequired() {
        return required;
    }
}
