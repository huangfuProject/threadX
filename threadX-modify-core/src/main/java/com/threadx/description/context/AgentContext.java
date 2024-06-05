package com.threadx.description.context;

import com.threadx.call.UpdateThreadPoolCall;
import com.threadx.constant.ThreadXPropertiesEnum;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.metrics.api.MetricsOutApi;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 应用上下文
 *
 * @author huangfukexing
 * @date 2023/3/10 18:05
 */
public class AgentContext {

    private static final AtomicBoolean BOOTSTRAP_STATE = new AtomicBoolean(false);

    /**
     * 服务名称
     */
    private static String serverName;

    /**
     * 实例名称
     */
    private static String instanceName;

    /**
     * 使用的classLoad
     */
    private static ClassLoader agentClassLoader;

    /**
     * 使用的instrumentation API
     */
    private static Instrumentation instrumentation;

    /**
     * 应用参数
     */
    private static String args;

    /**
     * agent包描述对象
     */
    private static AgentPackageDescription agentPackageDescription;

    /**
     * 指标输出
     */
    private static MetricsOutApi metrics;

    /**
     * 修改的回调
     */
    private static UpdateThreadPoolCall updateThreadPoolCall;

    /**
     * 注册一个自定义的类加载器
     *
     * @param agentClassLoader 类加载器
     */
    public static void registerClassLoader(ClassLoader agentClassLoader) {
        AgentContext.agentClassLoader = agentClassLoader;
    }

    /**
     * 注册一个 Instrumentation API接口
     *
     * @param instrumentation API接口
     */
    public static void registerInstrumentation(Instrumentation instrumentation) {
        AgentContext.instrumentation = instrumentation;
    }

    /**
     * 注册AGENT参数
     *
     * @param args 参数信息
     */
    public static void registerAgentArgs(String args) {
        AgentContext.args = args;
    }

    /**
     * 注册一个agent包的解析信息
     *
     * @param agentPackageDescription agent包的解析信息
     */
    public static void registerAgentPackageDescription(AgentPackageDescription agentPackageDescription) {
        AgentContext.serverName = agentPackageDescription.getEnvProperties().getProperty(ThreadXPropertiesEnum.SERVER_MAKE_NAME.getKey());
        AgentContext.instanceName = agentPackageDescription.getEnvProperties().getProperty(ThreadXPropertiesEnum.INSTANCE_MAKE_NAME.getKey());
        AgentContext.agentPackageDescription = agentPackageDescription;
    }

    public static ClassLoader getAgentClassLoader() {
        return agentClassLoader;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public static String getArgs() {
        return args;
    }

    public static AgentPackageDescription getAgentPackageDescription() {
        return agentPackageDescription;
    }

    /**
     * 启动程序
     *
     * @return 返回程序是否已经启动过
     */
    public static boolean start() {
        return BOOTSTRAP_STATE.getAndSet(true);
    }

    public static MetricsOutApi getMetrics() {
        return metrics;
    }

    public static void setMetrics(MetricsOutApi metrics) {
        AgentContext.metrics = metrics;
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getInstanceName() {
        return instanceName;
    }

    public static UpdateThreadPoolCall getUpdateThreadPoolCall() {
        return updateThreadPoolCall;
    }

    public static void setUpdateThreadPoolCall(UpdateThreadPoolCall updateThreadPoolCall) {
        AgentContext.updateThreadPoolCall = updateThreadPoolCall;
    }
}
