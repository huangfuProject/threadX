package com.threadx.check.impl;

import com.threadx.check.ThreadPoolExecutorInterceptCheck;
import com.threadx.constant.ThreadXPropertiesEnum;
import com.threadx.description.context.AgentContext;
import com.threadx.utils.ThreadXThreadPoolUtil;

import java.util.Properties;

/**
 * 是不是需要被拦截的包前缀
 *
 * @author huangfukexing
 * @date 2023/3/14 14:27
 */
public class IncludeTokenThreadPoolExecutorInterceptCheck extends ThreadPoolExecutorInterceptCheck {

    @Override
    public boolean interceptCheck() {
        String threadPoolName = ThreadXThreadPoolUtil.generateThreadPoolGroupName();
        //获取配置
        Properties envProperties = AgentContext.getAgentPackageDescription().getEnvProperties();
        String threadPoolInterceptPrefix = envProperties.getProperty(ThreadXPropertiesEnum.THREAD_POOL_INTERCEPT_PREFIX.getKey(), ThreadXPropertiesEnum.THREAD_POOL_INTERCEPT_PREFIX.getDefaultValue());
        if(ThreadXPropertiesEnum.THREAD_POOL_INTERCEPT_PREFIX.getDefaultValue().equals(threadPoolInterceptPrefix)) {
            return false;
        }
        return threadPoolName.startsWith(threadPoolInterceptPrefix);
    }
}
