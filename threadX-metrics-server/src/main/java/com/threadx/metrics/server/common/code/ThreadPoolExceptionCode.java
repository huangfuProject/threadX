package com.threadx.metrics.server.common.code;

/**
 * *************************************************<br/>
 * 权限异常的错误码<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:23
 */
public enum ThreadPoolExceptionCode implements IExceptionCode {
    /**
     * 不存在的线程池信息
     */
    NOT_EXIST_THREAD_POOL_DATA("560000", "不存在的线程池信息！"),

    /**
     * 线程池处于断连状态
     */
    THREAD_POOL_DISCONNECTION("560001", "线程池处于断连状态！"),
    ;
    /**
     * 错误吗
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    ThreadPoolExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
