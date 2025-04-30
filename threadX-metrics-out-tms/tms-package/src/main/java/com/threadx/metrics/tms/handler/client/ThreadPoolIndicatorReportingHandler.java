package com.threadx.metrics.tms.handler.client;


import cn.hutool.json.JSONUtil;
import com.threadx.metrics.tms.packages.ThreadPoolDataPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 线程池指标处理器
 *
 * @author huangfukexing
 * @date 2025/4/29 12:02
 */
@ChannelHandler.Sharable
public class ThreadPoolIndicatorReportingHandler extends SimpleChannelInboundHandler<ThreadPoolDataPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ThreadPoolDataPacket threadPoolDataPacket) throws Exception {
        System.out.println(JSONUtil.toJsonStr(threadPoolDataPacket));
    }
}
