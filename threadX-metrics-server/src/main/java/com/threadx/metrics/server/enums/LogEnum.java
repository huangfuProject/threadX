package com.threadx.metrics.server.enums;

/**
 * *************************************************<br/>
 * 日志信息枚举<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:25
 */
public enum LogEnum {
    /**
     * 添加用户
     */
    ADD_USER("100000", "添加用户"),
    /**
     * 管理修改用户
     */
    MANAGER_UPDATE_USER("100001", "管理修改用户"),
    /**
     * 用户登录
     */
    USER_LOGIN("100002", "用户登录"),

    /**
     * 用户登出
     */
    USER_LOGOUT("100003", "用户登出"),

    /**
     * 查询实例详情
     */
    QUERY_INSTANCE_DESC("100004", "查询实例详情"),


    ;
    /**
     * 日志标记
     */
    private final String activeKey;
    /**
     * 日志消息
     */
    private final String logMessage;

    LogEnum(String activeKey, String logMessage) {
        this.activeKey = activeKey;
        this.logMessage = logMessage;
    }

    public String getActiveKey() {
        return activeKey;
    }

    public String getLogMessage() {
        return logMessage;
    }
}
