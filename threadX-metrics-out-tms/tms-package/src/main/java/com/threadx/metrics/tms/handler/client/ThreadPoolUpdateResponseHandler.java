package com.threadx.metrics.tms.handler.client;

import com.threadx.communication.common.handlers.SyncChannelInboundHandler;
import com.threadx.metrics.tms.packages.ThreadPoolUpdateResponsePacket;
import io.netty.channel.ChannelHandler;

/**
 * 更新响应器
 *
 * @author huangfukexing
 * @date 2025/4/29 15:39
 */
@ChannelHandler.Sharable
public class ThreadPoolUpdateResponseHandler extends SyncChannelInboundHandler<ThreadPoolUpdateResponsePacket> {
}
