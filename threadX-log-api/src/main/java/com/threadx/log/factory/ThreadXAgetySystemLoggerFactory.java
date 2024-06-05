package com.threadx.log.factory;

import com.threadx.description.context.AgentContext;
import com.threadx.log.Logger;
import com.threadx.log.ThreadXLoggerFactoryApi;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工厂
 *
 * @author huangfukexing
 * @date 2023/3/11 20:57
 */
public class ThreadXAgetySystemLoggerFactory {

    private final static ConcurrentHashMap<String, Logger> LOGGER_CACHE = new ConcurrentHashMap<>(8);

    /**
     * 获取日志加载器
     *
     * @param targetClass class
     * @return 日志操作器
     */
    public static Logger getLogger(Class<?> targetClass) {
        String className = targetClass.getName();
        if (LOGGER_CACHE.containsKey(className)) {
            return LOGGER_CACHE.get(className);
        }
        Thread currentThread = Thread.currentThread();
        ClassLoader before = currentThread.getContextClassLoader();
        try {
            ClassLoader agentClassLoader = AgentContext.getAgentClassLoader();
            currentThread.setContextClassLoader(agentClassLoader);
            Class<?> loadClass = agentClassLoader.loadClass("com.threadx.log.LoggerSlf4jLog4j2");
            ThreadXLoggerFactoryApi loggerFactory = (ThreadXLoggerFactoryApi) loadClass.newInstance();
            Logger logger = loggerFactory.getLogger(targetClass);
            LOGGER_CACHE.put(className, logger);
            return logger;
        } catch (Throwable w) {
            throw new RuntimeException(w);
        } finally {
            currentThread.setContextClassLoader(before);
        }
    }
}
