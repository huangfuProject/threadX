package com.threadx.log;

/**
 * 日志托管
 *
 * @author huangfukexing
 * @date 2023/3/11 20:38
 */
public interface Logger {
    /**
     * 是否开启了trace
     *
     * @return 是否开启
     */
    boolean isTraceEnabled();

    /**
     * trace 日志
     *
     * @param var1 message
     */
    void trace(String var1);

    /**
     * trace 日志
     *
     * @param var1 模板
     * @param var2 参数填充
     */
    void trace(String var1, Object... var2);

    /**
     * trace 日志
     *
     * @param var1 模板
     * @param var2 异常
     */
    void trace(String var1, Throwable var2);

    /**
     * 是否开启debug
     *
     * @return 是否开启
     */
    boolean isDebugEnabled();

    /**
     * debug日志
     *
     * @param var1 消息
     */
    void debug(String var1);

    /**
     * debug日志
     *
     * @param var1 模版
     * @param var2 信息
     */
    void debug(String var1, Object... var2);

    /**
     * debug日志
     *
     * @param var1 模版
     * @param var2 异常
     */
    void debug(String var1, Throwable var2);

    /**
     * 是否开启info
     *
     * @return 是否开启
     */
    boolean isInfoEnabled();

    /**
     * info打印
     *
     * @param var1 消息
     */
    void info(String var1);

    /**
     * info 打印
     *
     * @param var1 模版
     * @param var2 参数信息
     */
    void info(String var1, Object... var2);

    /**
     * info 打印
     *
     * @param var1 模版
     * @param var2 异常
     */
    void info(String var1, Throwable var2);

    /**
     * 是否开启警告
     *
     * @return 是否开启
     */
    boolean isWarnEnabled();

    /**
     * warn日志
     *
     * @param var1 消息
     */
    void warn(String var1);

    /**
     * warn日志
     *
     * @param var1 日志信息
     * @param var2 数据参数
     */
    void warn(String var1, Object... var2);

    /**
     * warn日志
     *
     * @param var1 日志信息
     * @param var2 异常信息
     */
    void warn(String var1, Throwable var2);


    /**
     * 是否开启错误级别
     *
     * @return 是否开启
     */
    boolean isErrorEnabled();

    /**
     * 错误打印
     *
     * @param var1 错误消息
     */
    void error(String var1);

    /**
     * 错误打印
     *
     * @param var1 消息
     * @param var2 参数消息
     */
    void error(String var1, Object... var2);

    /**
     * 错误打印
     *
     * @param var1 消息
     * @param var2 错误消息
     */
    void error(String var1, Throwable var2);


}
