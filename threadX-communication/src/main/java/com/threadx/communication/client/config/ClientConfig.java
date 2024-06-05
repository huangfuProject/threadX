package com.threadx.communication.client.config;

import com.threadx.communication.common.DefaultMessageCommunicationConfig;
import com.threadx.communication.common.MessageCommunicationConfig;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端配置
 *
 * @author huangfukexing
 * @date 2023/4/7 13:59
 */
@Data
@AllArgsConstructor
public class ClientConfig implements Serializable {
    private static final long serialVersionUID = -6139324926693061420L;

    /**
     * 启动配置项
     */
    private MessageCommunicationConfig messageCommunicationConfig = new DefaultMessageCommunicationConfig();
    /**
     * 服务端处理器
     */
    private Map<String, ChannelInboundHandlerAdapter> channelInboundHandlerAdapterMap = new ConcurrentHashMap<>();

    /**
     * 主机名
     */
    private final String host;

    /**
     * 端口号
     */
    private final Integer port;

    /**
     * 服务标识
     */
    private final String serverKey;

    /**
     * 实例标识
     */
    private final String instanceKey;

    public ClientConfig(String host, Integer port, String serverKey, String instanceKey) {
        this.host = host;
        this.port = port;
        this.serverKey = serverKey;
        this.instanceKey = instanceKey;
    }

    public void addHandler(String handlerName, ChannelInboundHandlerAdapter channelInboundHandlerAdapter) {
        channelInboundHandlerAdapterMap.put(handlerName, channelInboundHandlerAdapter);
    }
}
