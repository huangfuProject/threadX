package com.threadx.metrics.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * *************************************************<br/>
 * 活跃的日志信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ActiveLog extends BaseEntity {
    private static final long serialVersionUID = -823408376812425631L;

    /**
     * 活动key标记
     */
    private String activeKey;

    /**
     * 行为日志
     */
    private String activeLog;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 操作耗时
     */
    private Long operationTime;

    /**
     * 活跃用户
     */
    private Long userId;



    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 系统信息
     */
    private String os;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 0 成功  1 失败
     */
    private String resultState;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 参数信息
     */
    private String paramData;


}
