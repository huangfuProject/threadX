package com.threadx.description.agent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/**
 * agent包的描述，描述他位置，依赖目录位置，配置目录位置，日志目录位置，采集到的数据目录位置
 *
 * @author huangfukexing
 * @date 2023/3/10 12:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentPackageDescription implements Serializable {
    private static final long serialVersionUID = -3407223672454305756L;

    /**
     * agent包的位置，绝对路径
     */
    private Path agentDirPath;

    /**
     * 依赖的位置，绝对路径
     */
    private Path libsDirPath;

    /**
     * 依赖信息的集合
     */
    private List<Path> libsPath;

    /**
     * 依赖的集合
     */
    private List<URL> libsUrls;

    /**
     * boot目录的 url
     */
    private Path bootDirPath;

    /**
     * boot目录中jar的 url
     */
    private List<File> bootJarFiles;

    /**
     * 配置文件夹所在的路径
     */
    private Path confDirPath;

    /**
     * 采集到的数据的路径信息
     */
    private Path dataDirPath;

    /**
     * 插件日志的位置
     */
    private Path logsDirPath;

    /**
     * 环境的配置信息
     */
    private Properties envProperties;


}
