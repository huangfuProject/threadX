package com.threadx.log;


/**
 * threadX的日志接口实现
 *
 * @author huangfukexing
 * @date 2023/3/11 20:28
 */
public abstract class ThreadXLoggerFactoryApi {

    public ThreadXLoggerFactoryApi() {
    }


    /**
     * 获取日志操作类
     *
     * @param targetClass 目标class
     * @return 返回对应的日志操作类
     */
    public abstract Logger getLogger(Class<?> targetClass);
}
