package com.threadx.metrics.server.handler;

import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateResponseMessage;
import com.threadx.communication.common.future.DefaultFuture;
import com.threadx.communication.common.handlers.ThreadXChannelInboundHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 线程池修改数据后返回的结果集
 *
 * @author huangfukexing
 * @date 2023/8/24 16:48
 */
@Slf4j
@Component
public class ThreadPoolUpdateResponseHandler extends ThreadXChannelInboundHandler<ThreadPoolUpdateResponseMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ThreadPoolUpdateResponseMessage threadPoolUpdateResponseMessage) throws Exception {
        DefaultFuture.received(threadPoolUpdateResponseMessage, channelHandlerContext.channel());
    }
}
