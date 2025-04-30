package com.threadx.communication.server.handler;

import com.threadx.communication.common.agreement.packet.HeartbeatMessage;
import com.threadx.communication.server.cache.ConnectionCache;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端心跳
 *
 * @author huangfukexing
 * @date 2023/4/25 14:32
 */
@ChannelHandler.Sharable
public class ServerHeartbeatHandler  extends SimpleChannelInboundHandler<HeartbeatMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatMessage msg) throws Exception {
        ConnectionCache.addConnection(ctx);
        ctx.channel().writeAndFlush(new HeartbeatMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ConnectionCache.closeChannelSilently(ctx, true);
    }
}
