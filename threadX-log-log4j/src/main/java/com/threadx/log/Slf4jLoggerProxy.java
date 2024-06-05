package com.threadx.log;

/**
 * 日志操作类代理
 *
 * @author huangfukexing
 * @date 2023/3/11 21:03
 */
public class Slf4jLoggerProxy implements Logger {

    private final org.apache.logging.log4j.Logger sourceLogger;

    public Slf4jLoggerProxy(org.apache.logging.log4j.Logger sourceLogger) {
        this.sourceLogger = sourceLogger;
    }

    @Override
    public boolean isTraceEnabled() {
        return sourceLogger.isTraceEnabled();
    }

    @Override
    public void trace(String var1) {
        sourceLogger.trace(var1);
    }

    @Override
    public void trace(String var1, Object... var2) {
        sourceLogger.trace(var1, var2);
    }

    @Override
    public void trace(String var1, Throwable var2) {
        sourceLogger.trace(var1, var2);
    }

    @Override
    public boolean isDebugEnabled() {
        return sourceLogger.isDebugEnabled();
    }

    @Override
    public void debug(String var1) {
        sourceLogger.debug(var1);
    }

    @Override
    public void debug(String var1, Object... var2) {
        sourceLogger.debug(var1, var2);
    }

    @Override
    public void debug(String var1, Throwable var2) {
        sourceLogger.debug(var1, var2);
    }

    @Override
    public boolean isInfoEnabled() {
        return sourceLogger.isInfoEnabled();
    }

    @Override
    public void info(String var1) {
        sourceLogger.info(var1);
    }

    @Override
    public void info(String var1, Object... var2) {
        sourceLogger.info(var1, var2);
    }

    @Override
    public void info(String var1, Throwable var2) {
        sourceLogger.info(var1, var2);
    }

    @Override
    public boolean isWarnEnabled() {
        return sourceLogger.isWarnEnabled();
    }

    @Override
    public void warn(String var1) {
        sourceLogger.warn(var1);
    }

    @Override
    public void warn(String var1, Object... var2) {
        sourceLogger.warn(var1, var2);
    }

    @Override
    public void warn(String var1, Throwable var2) {
        sourceLogger.warn(var1, var2);
    }

    @Override
    public boolean isErrorEnabled() {
        return sourceLogger.isErrorEnabled();
    }

    @Override
    public void error(String var1) {
        sourceLogger.error(var1);
    }

    @Override
    public void error(String var1, Object... var2) {
        sourceLogger.error(var1, var2);
    }

    @Override
    public void error(String var1, Throwable var2) {
        sourceLogger.error(var1, var2);
    }
}
