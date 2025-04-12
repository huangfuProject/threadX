package com.threadx.communication.handler;

import com.threadx.communication.pv.TestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author huangfukexing
 * @date 2025/4/12 21:05
 */
@ChannelHandler.Sharable
public class TestMessageHandler extends SimpleChannelInboundHandler<TestMessage> {

    final LongAdder longAdder = new LongAdder();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TestMessage testMessage) throws Exception {
        //System.out.printf("收到消息：%s, 累计消息数量：%d\n", testMessage.getName(), COUNT.incrementAndGet());
        longAdder.increment();
        long i  = longAdder.sum();
        if(i % 10000 == 0) {

            System.out.printf("累计消息数量：%d\n", i);
        }

    }
}
