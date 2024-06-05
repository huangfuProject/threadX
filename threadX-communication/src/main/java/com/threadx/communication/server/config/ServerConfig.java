package com.threadx.communication.server.config;

import com.threadx.communication.common.DefaultMessageCommunicationConfig;
import com.threadx.communication.common.MessageCommunicationConfig;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端的配置项
 *
 * @author huangfukexing
 * @date 2023/4/7 13:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerConfig implements Serializable {
    private static final long serialVersionUID = -3332101060115635093L;
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
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    public ServerConfig(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public ServerConfig(Integer port) {
        this.port = port;
    }

    /**
     * 新增一个处理器
     *
     * @param handlerName                  处理器名称
     * @param channelInboundHandlerAdapter 处理器
     */
    public void addChannelInboundHandler(String handlerName, ChannelInboundHandlerAdapter channelInboundHandlerAdapter) {
        channelInboundHandlerAdapterMap.put(handlerName, channelInboundHandlerAdapter);
    }
}
