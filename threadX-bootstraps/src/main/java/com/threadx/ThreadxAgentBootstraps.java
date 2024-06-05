package com.threadx;

import com.threadx.calculation.ThreadPoolIndicatorCollection;
import com.threadx.constant.ThreadXPropertiesEnum;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.description.context.AgentContext;
import com.threadx.metrics.api.MetricsOutApi;
import com.threadx.parse.AgentPathParse;
import com.threadx.utils.ThreadXCollectionUtils;
import sun.awt.AppContext;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * @author huangfukexing
 * @date 2023/3/9 10:28
 */
public class ThreadxAgentBootstraps {
    static final Logger logger = Logger.getGlobal();

    public static void premain(String agentArg, Instrumentation inst) {

        logger.info("ThreadX.BootStraps agentArgs: " + agentArg);
        logger.info("ThreadX.ClassLoader: " + ThreadxAgentBootstraps.class.getClassLoader());
        logger.info("App.ContextClassLoader: " + Thread.currentThread().getContextClassLoader());
        //正常情况下 ThreadPoolExecutorAgent的加载需要 Boot类加载器加载，否则会出现ClassNotDef异常
        if (Object.class.getClassLoader() != ThreadxAgentBootstraps.class.getClassLoader()) {
            logger.info("Invalid threadX-bootstraps.jar:" + agentArg);
            return;
        }

        if (AgentContext.start()) {
            logger.info("The agent has been started or is being started.");
            return;
        }

        try {
            //注册参数
            AgentContext.registerAgentArgs(agentArg);
            //注册代理器修改器
            AgentContext.registerInstrumentation(inst);
            //注册agent信息
            AgentContext.registerAgentPackageDescription(AgentPathParse.parse());
            //注册boot依赖，这里面的依赖，都被bootStrapClassLoad加载，保证绝对可用
            registerBootLib();
            //注册一个自定义的ClassLoad，该ClassLoader致力与加载libs目录下的程序，保证与应用程序分开，防止jar冲突
            registerAgentClassLoader();
            //启动代理程序
            start();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    /**
     * 注册boot依赖，这里面的依赖，都被bootStrapClassLoad加载，保证绝对可用
     *
     * @throws IOException 异常信息
     */
    private static void registerBootLib() throws IOException {
        Instrumentation instrumentation = AgentContext.getInstrumentation();
        AgentPackageDescription agentPackageDescription = AgentContext.getAgentPackageDescription();
        //获取应用程序内在boot目录的jar
        List<File> bootJarFiles = agentPackageDescription.getBootJarFiles();
        if (ThreadXCollectionUtils.isNotEmpty(bootJarFiles)) {
            //将 boot目录的jar也添加到 BootstrapClassLoader 的加载器中
            for (File bootJarFile : bootJarFiles) {
                logger.info("add boot jar:" + bootJarFile.getAbsolutePath());
                instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(bootJarFile));
            }
        }
    }

    /**
     * 启动应用程序，加载类修改器，执行原始类的修改
     *
     * @throws Exception 异常
     */
    private static void start() throws Exception {
        ClassLoader classLoader = AgentContext.getAgentClassLoader();
        if (classLoader != null) {
            startTargetConsumer(classLoader);
            //获取一个类修改器
            String runIngClassName = getBootStrapClassName();
            //使用自定义类加载器加载，保证他在下层，并且随时可用
            Class<?> bootClass = classLoader.loadClass(runIngClassName);
            final Thread currentThread = Thread.currentThread();
            //获取当前线程的类加载器
            final ClassLoader before = currentThread.getContextClassLoader();
            ModifyApplication modifyApplication;
            try {
                //使用自定义类加载器去创建这个对象
                currentThread.setContextClassLoader(classLoader);
                Constructor<?> constructor = bootClass.getDeclaredConstructor();
                modifyApplication = (ModifyApplication) constructor.newInstance();
                //初始化指标收集器  spi获取 因为已经将lib目录加入到当前的类加载器  所以这里能获取到全部的
                ServiceLoader<MetricsOutApi> metricsOutApis = ServiceLoader.load(MetricsOutApi.class, classLoader);
                if (ThreadXCollectionUtils.isNotEmpty(metricsOutApis)) {
                    //获取 使用的指标收集器的名称
                    Properties envProperties = AgentContext.getAgentPackageDescription().getEnvProperties();
                    //获取配置的指标收集器名称
                    String outModuleName = envProperties.getProperty(ThreadXPropertiesEnum.THREADX_METRICS_OUT_MODEL.getKey(), ThreadXPropertiesEnum.THREADX_METRICS_OUT_MODEL.getDefaultValue());
                    for (MetricsOutApi metricsOutApi : metricsOutApis) {
                        String metricsName = metricsOutApi.getMetricsName();
                        if (outModuleName.equals(metricsName)) {
                            //初始化指标收集器
                            metricsOutApi.init();
                            //将这个指标记录起来 保存起来
                            AgentContext.setMetrics(metricsOutApi);
                            break;
                        }
                    }
                }
            } finally {
                //恢复原始的类加载器  不然后续的应用程序启动会有问题
                currentThread.setContextClassLoader(before);
            }
            //启动修改器
            modifyApplication.start();
            //启动线程池指标收集器
            ThreadPoolIndicatorCollection.collection();
        }
    }

    /**
     * 启动消息的广播平台，具体需要根据配置启动一个广播平台
     *
     * @param classLoader 类加载器
     */
    private static void startTargetConsumer(ClassLoader classLoader) {
        logger.info("started Target Consumer.");
    }

    /**
     * 返回一个类修改器的实现方式，暂时只实现asm字节码的修改
     *
     * @return 类修改器的实现方式
     */
    private static String getBootStrapClassName() {
        return "com.threadx.handler.AsmModifyApplicationHandler";
    }

    /**
     * 注册依赖 形成自定义类加载器
     */
    public static void registerAgentClassLoader() {
        AgentPackageDescription agentPackageDescription = AgentContext.getAgentPackageDescription();
        List<URL> libsUrls = agentPackageDescription.getLibsUrls();

        if (ThreadXCollectionUtils.isNotEmpty(libsUrls)) {
            URL[] libUrls = libsUrls.toArray(new URL[0]);
            //创建一个类加载器，将libs目录的jar由自定义类加载器加载，同时自定义类加载器的父加载器使用BootstrapClassLoader的类加载器
            AgentClassLoader agentClassLoader = new AgentClassLoader(libUrls, ThreadxAgentBootstraps.class.getClassLoader());
            AgentContext.registerClassLoader(agentClassLoader);
        }
    }

}
