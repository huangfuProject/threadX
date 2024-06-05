package com.threadx.metrics.tms.handler;

import com.threadx.call.UpdateThreadPoolCall;
import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateRequestMessage;
import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateResponseMessage;
import com.threadx.communication.common.handlers.ThreadXChannelInboundHandler;
import com.threadx.description.context.AgentContext;
import com.threadx.metrics.ThreadPoolParam;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池参数修改
 *
 * @author huangfukexing
 * @date 2023/8/24 17:15
 */
public class ThreadPoolParamUpdateHandler extends ThreadXChannelInboundHandler<ThreadPoolUpdateRequestMessage> {
    private final static AtomicInteger THREAD_ID = new AtomicInteger(0);

    private final static ThreadPoolExecutor UPDATE_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(8, 32, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2048), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(String.format("threadx-update-ThreadPool:%s", THREAD_ID.getAndIncrement()));
            return thread;
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ThreadPoolUpdateRequestMessage msg) throws Exception {
        UPDATE_THREAD_POOL_EXECUTOR.execute(() -> updateThreadPool(ctx, msg));

    }

    private void updateThreadPool(ChannelHandlerContext ctx, ThreadPoolUpdateRequestMessage requestMessage) {
        UpdateThreadPoolCall updateThreadPoolCall = AgentContext.getUpdateThreadPoolCall();
        ThreadPoolParam param = new ThreadPoolParam();
        param.setObjectId(requestMessage.getObjectId());
        param.setCoreSize(requestMessage.getCoreSize());
        param.setMaximumPoolSize(requestMessage.getMaximumPoolSize());
        param.setKeepAliveTime(requestMessage.getKeepAliveTime());
        param.setRejectedExecutionHandlerClass(requestMessage.getRejectedExecutionHandlerClass());
        ThreadPoolUpdateResponseMessage responseMessage = new ThreadPoolUpdateResponseMessage();
        try {
            updateThreadPoolCall.modify(param);
            responseMessage.setSuccess(true);
        }catch (Exception e) {
            responseMessage.setSuccess(false);
            responseMessage.setErrorMessage("修改线程池失败:" + e.getMessage());
        }finally {
            responseMessage.setMessageId(requestMessage.getMessageId());
            ctx.channel().writeAndFlush(responseMessage);
        }

    }
}
