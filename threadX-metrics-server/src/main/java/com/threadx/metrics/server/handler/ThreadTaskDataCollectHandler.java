package com.threadx.metrics.server.handler;

import com.threadx.communication.common.agreement.packet.ThreadPoolTaskCollectMessage;
import com.threadx.communication.common.handlers.ThreadXChannelInboundHandler;
import com.threadx.communication.common.utils.ChannelUtil;
import com.threadx.metrics.server.async.ThreadTaskDataRunnable;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池中的任务数据处理器
 * 线程池任务相关的采集数据会发送到这个处理器中
 *
 * @author huangfukexing
 * @date 2023/4/7 13:37
 */
@Slf4j
@Component
public class ThreadTaskDataCollectHandler extends ThreadXChannelInboundHandler<ThreadPoolTaskCollectMessage> {


    private final static int SYSTEM_CORE_COUNT = Runtime.getRuntime().availableProcessors();

    private final static ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(SYSTEM_CORE_COUNT, SYSTEM_CORE_COUNT * 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024), new ThreadPoolExecutor.DiscardOldestPolicy());



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ThreadPoolTaskCollectMessage taskCollectMessage) throws Exception {
        THREAD_POOL_EXECUTOR.execute(new ThreadTaskDataRunnable(taskCollectMessage, ChannelUtil.getChannelRemoteAddress(channelHandlerContext.channel())));
    }
}
