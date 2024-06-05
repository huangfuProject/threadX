package com.threadx.metrics.server.common.code;

/**
 * 异常信息的错误码
 *
 * @author huangfu
 * @date 2022年11月1日17:53:14
 */
public interface IExceptionCode {

    /**
     * 返回错误码
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 返回错误信息
     *
     * @return 错误信息
     */
    String getMessage();

    /**
     * 默认的消息组合方式
     *
     * @return 错误消息组装
     */
    default String defaultMessage() {
        return String.format("%s:%s", getMessage(), getCode());
    }
}
