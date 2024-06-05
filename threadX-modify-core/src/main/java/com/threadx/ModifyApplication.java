package com.threadx;

import com.threadx.description.context.AgentContext;

import java.lang.instrument.UnmodifiableClassException;

/**
 * 修改应用程序的上下文
 *
 * @author huangfukexing
 * @date 2023/3/10 18:10
 */
public abstract class ModifyApplication {

    /**
     * 启动应用程序上下文
     * @throws Exception 异常
     */
    public abstract void start() throws Exception;
}
