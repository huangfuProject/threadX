package com.threadx.metrics.tms.handler.server;

import cn.hutool.json.JSONUtil;
import com.threadx.metrics.tms.packages.ThreadPoolUpdateRequestPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 线程池修改事件处理器
 *
 * @author huangfukexing
 * @date 2025/4/29 15:35
 */
@ChannelHandler.Sharable
public class ThreadPoolUpdateRequestHandler extends SimpleChannelInboundHandler<ThreadPoolUpdateRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ThreadPoolUpdateRequestPacket threadPoolUpdateRequestPacket) throws Exception {
        System.out.println(JSONUtil.toJsonStr(threadPoolUpdateRequestPacket));
    }
}
