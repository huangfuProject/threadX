package com.threadx.metrics.log4j;

import cn.hutool.json.JSONUtil;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.description.context.AgentContext;
import com.threadx.log.Logger;
import com.threadx.log.LoggerSlf4jLog4j2;
import com.threadx.metrics.ThreadPoolExecutorData;
import com.threadx.metrics.ThreadTaskExecutorData;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.metrics.log4j.config.ThreadXMetricsLog4jPropertiesEnum;

import java.util.List;
import java.util.Properties;

/**
 * log4j输出的指标
 *
 * @author huangfukexing
 * @date 2023/3/27 22:15
 */
public class MetricsOutLog4j implements MetricsOutApi {

    private final static String METRICS_NAME = "log4j";

    private LoggerSlf4jLog4j2 loggerSlf4jLog4j2;

    private Logger logger;


    @Override
    public void init() {
        AgentPackageDescription agentPackageDescription = AgentContext.getAgentPackageDescription();
        Properties envProperties = agentPackageDescription.getEnvProperties();
        //获取日志配置的位置
        String log4jPathConfigPath = envProperties.getProperty(ThreadXMetricsLog4jPropertiesEnum.THREADX_METRICS_OUT_LOG_CONFIG_PATH.getKey(), ThreadXMetricsLog4jPropertiesEnum.THREADX_METRICS_OUT_LOG_CONFIG_PATH.getDefaultValue());
        if (ThreadXMetricsLog4jPropertiesEnum.THREADX_METRICS_OUT_LOG_CONFIG_PATH.getDefaultValue().equals(log4jPathConfigPath)) {
            loggerSlf4jLog4j2 = new LoggerSlf4jLog4j2(log4jPathConfigPath);
        } else {
            loggerSlf4jLog4j2 = new LoggerSlf4jLog4j2();
        }
        logger = loggerSlf4jLog4j2.getLogger(MetricsOutLog4j.class);
    }

    @Override
    public void outThreadPoolMetricsData(ThreadPoolExecutorData metricsData) {
        logger.info(JSONUtil.toJsonStr(metricsData));
    }

    @Override
    public void outThreadTaskMetricsData(ThreadTaskExecutorData metricsData) {
        logger.info(JSONUtil.toJsonStr(metricsData));
    }


    @Override
    public void destroy() {
        loggerSlf4jLog4j2 = null;
    }

    @Override
    public String getMetricsName() {
        return METRICS_NAME;
    }
}
