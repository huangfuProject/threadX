package com.threadx.communication.common.handlers;

import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.future.DefaultFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 同步消息管理模型
 *
 * @author huangfukexing
 * @date 2025/4/29 09:19
 */
@ChannelHandler.Sharable
public class SyncChannelInboundHandler<I extends Message> extends SimpleChannelInboundHandler<I> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, I message) throws Exception {
        if(message.isSync()) {
            DefaultFuture.received(message, channelHandlerContext.channel());
        }

    }


}
