package com.threadx.communication.server;

import com.threadx.communication.common.agreement.packet.SyncMessage;
import com.threadx.communication.common.future.DefaultFuture;
import com.threadx.communication.server.cache.ConnectionCache;
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
     * @param address     客户端的地址
     * @param syncMessage 要发送的消息
     * @return 结果集
     */
    public static SyncMessage syncSendMessage(String address, SyncMessage syncMessage) {
        ChannelHandlerContext connection = ConnectionCache.getConnection(address);
        if (connection == null) {
            throw new RuntimeException(String.format("The client【%s】 connection is not active.", address));
        }
        Channel channel = connection.channel();
        channel.writeAndFlush(syncMessage);
        DefaultFuture defaultFuture = DefaultFuture.newDefaultFuture(syncMessage, channel, 30);
        try {
            Object resultObject = defaultFuture.get();
            if (resultObject instanceof Exception) {
                throw new RuntimeException((Throwable) resultObject);
            }
            if (resultObject instanceof SyncMessage) {
                return (SyncMessage) resultObject;
            } else {
                throw new RuntimeException("The result set received is not data of the synchronous message type.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
