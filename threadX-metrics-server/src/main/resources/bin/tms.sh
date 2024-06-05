#!/bin/bash
# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "$(dirname "${BASH_SOURCE[0]}")")" && pwd)"
# 获取工程目录
TMS_DIR="$(dirname "$SCRIPT_DIR")"

# 定义应用程序的相关信息
APP_NAME="threadX-metrics-server"
VERSION="1.0-SNAPSHOT"  # 请替换为你的应用程序版本号

# 定义应用程序的路径
APP_DIR="$TMS_DIR/lib"
LOG_DIR="$TMS_DIR/logs"
CONFIG_DIR="$TMS_DIR/conf"
JAR_FILE="$APP_DIR/$APP_NAME-${VERSION}.jar"
CONFIG_FILE="$CONFIG_DIR/application.properties"
LOG_CONFIG_FILE="$CONFIG_DIR/logback-spring.xml"
PAGE_DIR="$TMS_DIR/pages"
JVM_XMX="2g"

# 创建日志目录（如果不存在）
mkdir -p $LOG_DIR

# 处理命令行参数
if [ "$1" == "start" ]; then
    echo "Starting $APP_NAME..."
    nohup java -Xms$JVM_XMX -Xmx$JVM_XMX -DlogPath=$LOG_DIR/app.log -Dlogging.config=$LOG_CONFIG_FILE -jar $JAR_FILE --spring.config.location=file:$CONFIG_FILE --spring.web.resources.static-locations=file:$PAGE_DIR >> $LOG_DIR/out.log 2>&1 &
    echo "$APP_NAME started."
elif [ "$1" == "stop" ]; then
    echo "Stopping $APP_NAME..."
    # 获取应用程序的进程 ID
    PID=$(ps aux | grep "$JAR_FILE" | grep -v grep | awk '{print $2}')
    if [ -n "$PID" ]; then
        # 使用 kill 命令停止应用程序
        kill $PID
        echo "$APP_NAME stopped."
    else
        echo "$APP_NAME is not running."
    fi
else
    echo "Usage: $0 {start|stop}"
    exit 1
fi
