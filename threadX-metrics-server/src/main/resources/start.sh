#!/bin/bash
# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "$(dirname "${BASH_SOURCE[0]}")")" && pwd)"

# 定义应用程序的相关信息
APP_NAME="threadX-metrics-server"
VERSION="1.0-SNAPSHOT"  # 请替换为你的应用程序版本号

# 定义应用程序的路径
APP_DIR="$SCRIPT_DIR/lib"
LOG_DIR="$SCRIPT_DIR/logs"
CONFIG_DIR="$SCRIPT_DIR/conf"
JAR_FILE="$APP_DIR/$APP_NAME-${VERSION}.jar"
CONFIG_FILE="$CONFIG_DIR/application.properties"
LOG_CONFIG_FILE="$CONFIG_DIR/logback-spring.xml"
JVM_XMX="2g"

# 创建日志目录（如果不存在）
mkdir -p $LOG_DIR

# 启动应用程序
nohub java -Xms$JVM_XMX -Xmx$JVM_XMX -DlogPath=$LOG_DIR/app.log -Dlogging.config=$LOG_CONFIG_FILE -jar $JAR_FILE --spring.config.location=file:$CONFIG_FILE >> $LOG_DIR/out.log 2>&1