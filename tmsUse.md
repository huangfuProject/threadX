# threadX-metrics-server部署手册

## 一、下载
[下载地址](https://github.com/huangfuProject/threadX/releases/download/v1.0.0-bate.1/threadX-1.0-SNAPSHOT.tar.gz)

## 二、安装线程收集器
```shell
#解压安装包
tar -zvxf threadX-1.0-SNAPSHOT.tar.gz
cd threadX-1.0-SNAPSHOT/threadX-Tms
```
## 三、服务安装
1. 进入到 'sql'目录下执行sql脚本
2. 进入到 'conf' 目录下修改配置文件 'application.properties'

## 四、启动服务
```shell
cd $THREADX_TMS_HOME/bin
sh tms.sh start
```
查看启动日志
```shell
cd $THREADX_TMS_HOME/logs
```

## 五、停止服务
```shell
cd $THREADX_TMS_HOME/bin
sh tms.sh stop
```

