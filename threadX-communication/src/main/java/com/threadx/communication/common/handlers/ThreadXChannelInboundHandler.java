package com.threadx.communication.common.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 简单的管道数据处理器基类
 *
 * @author huangfukexing
 * @date 2023/4/12 15:30
 */
@ChannelHandler.Sharable
public abstract class ThreadXChannelInboundHandler<I> extends SimpleChannelInboundHandler<I> {
}
