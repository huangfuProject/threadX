package com.threadx.constant;

/**
 * 常量值定义
 *
 * @author huangfukexing
 * @date 2023/3/15 10:00
 */
public enum ThreadPoolProxyMake {
    /**
     * 被代理
     */
    THREAD_X_PROXY("ThreadX", "被代理的对象"),
    /**
     * 不被代理
     */
    NOT_PROXY("default", "没有被代理的对象"),
    ;
    /**
     * 代理标记
     */
    private final String make;
    /**
     * 解释
     */
    private final String desc;


    ThreadPoolProxyMake(String make, String desc) {
        this.make = make;
        this.desc = desc;
    }

    public String getMake() {
        return make;
    }

    public String getDesc() {
        return desc;
    }
}
