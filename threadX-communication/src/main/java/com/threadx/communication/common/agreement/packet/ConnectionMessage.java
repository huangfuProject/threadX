package com.threadx.communication.common.agreement.packet;

import java.io.Serializable;

/**
 * 客户端连接的消息
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
public class ConnectionMessage extends Message implements Serializable {
    private static final long serialVersionUID = 4659504775850578237L;
    /**
     * 连接状态
     */
    private ConnectionStatus connectionStatus = ConnectionStatus.OPEN;


    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public enum ConnectionStatus {
        /**
         * 开启
         */
        OPEN,
        /**
         * 销毁
         */
        CLOSE
    }
}

