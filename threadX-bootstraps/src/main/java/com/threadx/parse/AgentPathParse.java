package com.threadx.parse;

import com.threadx.constant.ThreadXPropertiesEnum;
import com.threadx.description.agent.AgentPackageDescription;
import com.threadx.utils.ThreadXCollectionUtils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 插件路径解析
 *
 * @author huangfukexing
 * @date 2023/3/10 12:45
 */
public class AgentPathParse {


    static final String JAVA_AGENT_OPTION = "-javaagent:";
    public static final String JAVA_AGENT_NAME = "threadX-bootstraps";
    static final String AGENT_CONF_FILE_NAME = "threadX.properties";
    static final Logger logger = Logger.getGlobal();

    /**
     * 解析agent程序对应的目录信息
     *
     * @return 返回目录信息
     */
    public static AgentPackageDescription parse() throws Exception {
        //获取当前agent程序的路径信息
        Path agentPath = findAgentPath();
        //获取当前agent所在目录
        Path agentParent = getAgentRootDir();
        //获取libs目录   libs主要存储着第三方依赖   由应用程序类加载器加载的依赖
        Path libsDirPath = agentParent.resolve("libs");
        //获取boot目录   boot目录主要存储需要由 BootstrapClassLoader 类加载器加载的依赖
        // 因为双亲委派模型的原因，threadPool由BootstrapClassLoader加载.
        // 所以直接操作修改 ThreadPoolExecutor的相关依赖需要由同级或者上级的类加载器加载
        Path bootDirPath = agentParent.resolve("boot");
        // 获取配置信息
        Path confDirPath = agentParent.resolve("conf");
        // 获取data目录
        Path dataDirPath = Paths.get(agentParent.toString(), "data");
        // 获取日志目录
        Path logsDirPath = Paths.get(agentParent.toString(), "logs");
        // 获取所有的依赖目录
        List<Path> orderLibPaths = findLibs(libsDirPath);
        List<URL> libsUrls = new ArrayList<>();
        // 转换为uri 方便加载
        if (ThreadXCollectionUtils.isNotEmpty(orderLibPaths)) {
            for (Path orderLibPath : orderLibPaths) {
                try {
                    logger.info("Search for lib dependencies: " + orderLibPath.toString());
                    URL url = orderLibPath.toUri().toURL();
                    libsUrls.add(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        //获取BootLib jar file
        List<File> bootLibJars = findBootLibJars();
        // 读取配置信息
        Properties envProperties = readEnvProperties(confDirPath);
        // 将上面解析的配置信息包装为对象
        return new AgentPackageDescription(agentPath, libsDirPath, orderLibPaths, libsUrls, bootDirPath, bootLibJars, confDirPath, dataDirPath, logsDirPath, envProperties);
    }

    /**
     * 读取环境的配置信息
     *
     * @param confDirPath 配置目录
     * @return 配置信息
     */
    private static Properties readEnvProperties(Path confDirPath) throws Exception {
        if (confDirPath == null) {
            throw new RuntimeException("Error agent config path, not found threadX config dir: ${THREADX_HOME}/conf");
        }
        File confDirFile = confDirPath.toFile();
        File file = new File(confDirFile, AGENT_CONF_FILE_NAME);
        if (!file.exists()) {
            throw new RuntimeException("Error agent config data, not found threadX config file: ${THREADX_HOME}/conf/threadX.properties");
        }
        //读取配置信息
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            logger.info("Load ${THREADX_HOME}/conf/threadX.properties data.");
            properties.load(reader);
        }
        //获取系统参数
        Properties systemProperties = System.getProperties();
        //将系统参数与配置文件参数合并，系统参数优先级高
        Set<String> propertyNames = systemProperties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            properties.put(propertyName, systemProperties.getProperty(propertyName));
        }
        //参数检查
        checkConfig(properties);
        return properties;
    }

    /**
     * 参数检查
     *
     * @param properties 要检查的配置信息
     */
    private static void checkConfig(Properties properties) {
        ThreadXPropertiesEnum[] threadXPropertiesEnums = ThreadXPropertiesEnum.values();
        for (ThreadXPropertiesEnum propertiesEnum : threadXPropertiesEnums) {
            if (propertiesEnum.isRequired()) {
                String propertiesEnumKey = propertiesEnum.getKey();
                String defaultValue = propertiesEnum.getDefaultValue();
                String requiredProperty = properties.getProperty(propertiesEnumKey, null);
                //如果必传的参数为空或者等于默认值 就直接报错
                if (requiredProperty == null || defaultValue.equals(requiredProperty)) {
                    throw new RuntimeException("threadX The configuration information is incorrect. The mandatory parameter is null. config name is " + propertiesEnumKey);
                }
            }
        }
    }


    /**
     * 获取插件的根目录
     *
     * @return 插件的根目录
     */
    private static Path getAgentRootDir() {
        //先获取当前agent的路径信息
        Path agentPath = findAgentPath();
        if (agentPath == null) {
            throw new RuntimeException("The location of threadX-bootstraps-${version}.jar is not found. Add -javaagent: the absolute path to your threadX-agent.jar on the startup command.");
        }
        //获取当前agent的上级目录
        return agentPath.getParent();
    }


    /**
     * 寻找boot目录的jar文件
     *
     * @return 其中所有的jar
     */
    public static List<File> findBootLibJars() {
        Path agentRootDir = getAgentRootDir();
        Path bootDirPath = agentRootDir.resolve("boot");
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(bootDirPath, "*.jar")) {
            List<File> list = new ArrayList<>();
            for (Path path : paths) {
                if (path != null) {
                    list.add(path.toFile());
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(bootDirPath + " Path IO error", e);
        }
    }

    /**
     * 查找依赖的集合
     *
     * @param libsDirPath 依赖目录
     * @return 依赖集合
     */
    private static List<Path> findLibs(Path libsDirPath) {
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(libsDirPath, "*.{jar,xml,properties}")) {
            List<Path> list = new ArrayList<>();
            for (Path path : paths) {
                list.add(path);
            }

            List<Path> orderList = new ArrayList<>();

            for (Path path : list) {
                Path fileName = path.getFileName();
                if (fileName != null && fileName.startsWith("threadX-")) {
                    orderList.add(path);
                }
            }

            for (Path path : list) {
                Path fileName = path.getFileName();
                if (fileName != null && !fileName.startsWith("threadX-")) {
                    orderList.add(path);
                }
            }
            return orderList;
        } catch (IOException e) {
            throw new RuntimeException(libsDirPath + " Path IO error", e);
        }
    }


    /**
     * 寻找插件的绝对路径信息
     *
     * @return 插件的绝对路径信息
     */
    private static Path findAgentPath() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        //获取java运行程序中所有的运行jvm参数
        List<String> inputArguments = runtimeMxBean.getInputArguments();
        for (String inputArgument : inputArguments) {
            String formatArgument = removeJavaagent(inputArgument);
            //寻找当前增强插件的绝对路径
            if (formatArgument.contains(JAVA_AGENT_NAME)) {
                return Paths.get(formatArgument);
            }
        }
        return null;

    }

    /**
     * 删除agent前缀
     *
     * @param argument 参数信息
     * @return 不带前缀的字符串
     */
    public static String removeJavaagent(String argument) {
        return argument.substring(JAVA_AGENT_OPTION.length());
    }
}
