package com.threadx.communication.server;

import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.future.DefaultFuture;
import com.threadx.communication.server.cache.ConnectionCache;
import com.threadx.communication.server.config.ServerConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务发送消息的操作类
 *
 * @author huangfukexing
 * @date 2023/8/24 16:35
 */
public class ServerSendMessage {

    /**
     * 同步发送消息  服务端想=向客户端发送消息i
     *
     * @param syncMessage  要发送的消息
     * @param serverConfig 服务配置
     * @return 结果集
     */
    public static Message syncSendMessage(Message syncMessage, ServerConfig serverConfig) {
        return syncSendMessage(syncMessage, serverConfig, new NoAvailableConnection() {
            @Override
            public Message callMessage(Message syncMessage, ServerConfig serverConfig) {
                return NoAvailableConnection.super.callMessage(syncMessage, serverConfig);
            }
        });
    }

    /**
     * 同步发送消息  服务端想=向客户端发送消息i
     *
     * @param syncMessage  要发送的消息
     * @param serverConfig 服务配置
     * @return 结果集
     */
    public static Message syncSendMessage(Message syncMessage, ServerConfig serverConfig, NoAvailableConnection noAvailableConnection) {
        ChannelHandlerContext connection = ConnectionCache.getConnection(serverConfig);
        if (connection == null) {
            return noAvailableConnection.callMessage(syncMessage, serverConfig);

        }
        Channel channel = connection.channel();
        channel.writeAndFlush(syncMessage);
        syncMessage.setSync(true);
        DefaultFuture defaultFuture = DefaultFuture.newDefaultFuture(syncMessage, channel, 30);
        try {
            Object resultObject = defaultFuture.get();
            if (resultObject instanceof Exception) {
                throw new RuntimeException((Throwable) resultObject);
            }
            if (resultObject instanceof Message) {
                return (Message) resultObject;
            } else {
                throw new RuntimeException("The result set received is not data of the synchronous message type.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步发送消息  服务端想=向客户端发送消息i
     *
     * @param syncMessage  要发送的消息
     * @param serverConfig 服务配置
     */
    public static void asyncSendMessage(Message syncMessage, ServerConfig serverConfig) {
        asyncSendMessage(syncMessage, serverConfig, new NoAvailableConnection() {
            @Override
            public void call(Message syncMessage, ServerConfig serverConfig) {
                NoAvailableConnection.super.call(syncMessage, serverConfig);
            }
        });
    }

    /**
     * 异步发送消息  服务端想=向客户端发送消息i
     *
     * @param syncMessage  要发送的消息
     * @param serverConfig 服务配置
     */
    public static void asyncSendMessage(Message syncMessage, ServerConfig serverConfig, NoAvailableConnection noAvailableConnection) {
        ChannelHandlerContext connection = ConnectionCache.getConnection(serverConfig);
        if (connection == null) {
            noAvailableConnection.call(syncMessage, serverConfig);

            return;
        }
        Channel channel = connection.channel();
        channel.writeAndFlush(syncMessage);
    }

    public static interface NoAvailableConnection {
        default void call(Message syncMessage, ServerConfig serverConfig) {
            System.out.println("There is no available connection.");
        }

        default Message callMessage(Message syncMessage, ServerConfig serverConfig) {
            System.out.println("There is no available connection.");
            return null;
        }
    }
}
