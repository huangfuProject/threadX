package com.threadx.metrics.tms.handler.client;


import cn.hutool.json.JSONUtil;
import com.threadx.metrics.tms.packages.ThreadPoolDataPacket;
import com.threadx.metrics.tms.packages.ThreadPoolTaskDataPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 线程池任务指标处理器
 *
 * @author huangfukexing
 * @date 2025/4/29 12:02
 */
@ChannelHandler.Sharable
public class ThreadPoolTaskIndicatorReportingHandler extends SimpleChannelInboundHandler<ThreadPoolTaskDataPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ThreadPoolTaskDataPacket threadPoolTaskDataPacket) throws Exception {
        System.out.println(JSONUtil.toJsonStr(threadPoolTaskDataPacket));
    }
}
