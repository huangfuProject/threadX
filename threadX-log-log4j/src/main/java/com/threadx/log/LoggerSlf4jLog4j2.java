package com.threadx.log;

import com.threadx.description.context.AgentContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;


/**
 * threadX日志工厂，日志全局配置
 *
 * @author huangfukexing
 * @date 2023/3/11 19:43
 */
public class LoggerSlf4jLog4j2 extends ThreadXLoggerFactoryApi {

    private final LoggerContext loggerContext;

    public LoggerSlf4jLog4j2() {
        this(new File(AgentContext.getAgentPackageDescription().getConfDirPath().toFile(), "log4j2.xml").getAbsolutePath());
    }

    public LoggerSlf4jLog4j2(String log4jConfigPath) {
        loggerContext = registerLogConfig(log4jConfigPath);
    }

    /**
     * 注册日志的配置文件
     *
     * @return LoggerContext 日志上下文
     */
    private LoggerContext registerLogConfig(String logConfigPath) {
        return Configurator.initialize("ThreadX-LogConfig", logConfigPath);
    }

    /**
     * 获取一个类加载器
     *
     * @param targetClass 目标Class
     * @return 返回对应的日志信息
     */
    @Override
    public Logger getLogger(Class<?> targetClass) {

        try {
            org.apache.logging.log4j.Logger logger = loggerContext.getLogger(targetClass);
            return new Slf4jLoggerProxy(logger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
