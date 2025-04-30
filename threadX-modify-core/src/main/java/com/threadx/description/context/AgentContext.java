package com.threadx.description.context;

import com.threadx.call.UpdateThreadPoolCall;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.metrics.api.MetricsOutApi;
import lombok.Getter;

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
     * 使用的classLoad
     */
    @Getter
    private static ClassLoader agentClassLoader;

    /**
     * 使用的instrumentation API
     */
    @Getter
    private static Instrumentation instrumentation;

    /**
     * 应用参数
     */
    @Getter
    private static String args;

    /**
     * agent包描述对象
     */
    @Getter
    private static AgentPackageDescription agentPackageDescription;

    /**
     * 指标输出
     */
    @Getter
    private static MetricsOutApi metrics;

    /**
     * 修改的回调
     */
    @Getter
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
        AgentContext.agentPackageDescription = agentPackageDescription;
    }


    /**
     * 启动程序
     *
     * @return 返回程序是否已经启动过
     */
    public static boolean start() {
        return BOOTSTRAP_STATE.getAndSet(true);
    }


    public static void setMetrics(MetricsOutApi metrics) {
        AgentContext.metrics = metrics;
    }


    public static void setUpdateThreadPoolCall(UpdateThreadPoolCall updateThreadPoolCall) {
        AgentContext.updateThreadPoolCall = updateThreadPoolCall;
    }
}
