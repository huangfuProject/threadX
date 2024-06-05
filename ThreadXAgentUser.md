## 安装插件
1. 下载插件：[下载地址](https://github.com/huangfuProject/threadX/releases)
2. 解压插件
    ```shell
    tar -zvxf threadX-1.0-SNAPSHOT.tar.gz
    cd threadX-1.0-SNAPSHOT/threadX-Agent
    ```
3. 使用插件
    ```shell
      java -javaagent:/$THREADX_AGENT_HOME/threadX-Agent/threadX-bootstraps.jar -jar xxx.jar
    ```
4. 必填参数信息
    ```properties
    threadx.server.name=服务名称
    threadx.instance.name=实例名称
    threadx.thread.pool.intercept.prefix=线程池的拦截前缀
    ```
5. 可选参数
    ```properties
    threadx.thread.pool.collection.interval=线程池采集间隔
    threadx.thread.pool.metrics.model=线程指标的输出配置(默认可选  log4j、tms)
    ```
   如果 `threadx.thread.pool.metrics.model=tms` 则收集服务地址必填：
   ```properties
   threadx.thread.pool.metrics.tms.address=host:port
    ```
6. 示例命令
    ```shell
    java -javaagent:/$THREADX_AGENT_HOME/threadX-Agent/threadX-bootstraps.jar /
   -Dthreadx.server.name=测试服务 -Dthreadx.instance.name=测试服务-实例02 \
   -Dthreadx.thread.pool.intercept.prefix=com.threadX.test \
   -Dthreadx.thread.pool.metrics.model=tms -Dthreadx.thread.pool.metrics.tms.address=127.0.0.1:9998
    ```
7. 配置文件配置
    ```shell
    vim /$THREADX_AGENT_HOME/threadX-Agent/conf/threadX.properties
    ```
    增加配置项
    ```properties
    threadx.server.name=服务名称
    threadx.instance.name=实例名称
    threadx.thread.pool.intercept.prefix=线程池的拦截前缀
    threadx.thread.pool.collection.interval=线程池采集间隔
    threadx.thread.pool.metrics.model=线程指标的输出配置(默认可选  log4j、tms)
    threadx.thread.pool.metrics.tms.address=host:port
    ```
   启动服务
    ```shell
    java -javaagent:/$THREADX_AGENT_HOME/threadX-Agent/threadX-bootstraps.jar -jar xxx.jar
    ```
8. 注意事项
   > /$THREADX_AGENT_HOME/threadX-Agent 下的目录严格保留，不允许自定义挪移位置