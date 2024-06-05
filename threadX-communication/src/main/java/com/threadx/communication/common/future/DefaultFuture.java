package com.threadx.communication.common.future;

import com.threadx.communication.common.agreement.packet.SyncMessage;
import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateRequestMessage;
import com.threadx.communication.common.agreement.packet.ThreadPoolUpdateResponseMessage;
import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 异步消息结果集
 *
 * @author huangfukexing
 * @date 2023/8/10 16:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultFuture extends CompletableFuture<Object> implements Serializable {
    private static final long serialVersionUID = 7109955063081226484L;

    /**
     * 超时检测定时器
     * <p>
     * HASHED_WHEEL_TIMER:工作轮盘
     * ThreadFactory：创建work线程
     * tickDuration:每个刻度的时间
     * ticsPerWheel:轮盘一圈大小
     * maxPendingTimeouts:最大等待处理超时
     */
    private static final HashedWheelTimer HASHED_WHEEL_TIMER = new HashedWheelTimer(r -> new Thread(r, "threadx-timeout-check" + r.hashCode()), 30, TimeUnit.MILLISECONDS, 8, true, 0);


    /**
     * 请求的消息对象
     */
    private SyncMessage requestSyncMessage;

    /**
     * 通讯管道
     */
    private Channel clientChannel;

    /**
     * 超时时间  秒
     */
    private int timeout;

    /**
     * 超时检测任务
     */
    private Timeout timeoutCheckTask;

    /**
     * 未来回复对象缓存
     */
    private final static Map<String, DefaultFuture> FUTURE_CACHE = new ConcurrentHashMap<>(32);

    private DefaultFuture(SyncMessage requestMessage, Channel clientChannel, int timeout) {
        String messageId = requestMessage.getMessageId();
        if (messageId != null && messageId.length() > 0) {
            this.requestSyncMessage = requestMessage;
            this.clientChannel = clientChannel;
            this.timeout = timeout;
            FUTURE_CACHE.put(messageId, this);
            this.timeoutCheckTask = HASHED_WHEEL_TIMER.newTimeout(new TimeoutCheckTimerTask(messageId), timeout, TimeUnit.SECONDS);
        } else {
            System.err.println("requestMessage message【messageId】is null, Please check the parameters! Request sent, but no result set!");
        }
    }


    /**
     * 创建一个消息处理器
     *
     * @param requestMessage 请求消息
     * @param clientChannel  消息管道
     * @param timeout        超时时间
     * @return 消息处理器
     */
    public static DefaultFuture newDefaultFuture(SyncMessage requestMessage, Channel clientChannel, int timeout) {
        return new DefaultFuture(requestMessage, clientChannel, timeout);
    }

    /**
     * 获取一个消息回复对象
     *
     * @param messageId 消息的id
     * @return 消息回复对象
     */
    public static DefaultFuture getFuture(String messageId) {
        return FUTURE_CACHE.get(messageId);
    }

    private static class TimeoutCheckTimerTask implements TimerTask {

        private final String messageId;

        private TimeoutCheckTimerTask(String messageId) {
            this.messageId = messageId;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            DefaultFuture future = DefaultFuture.getFuture(messageId);
            if (future == null || future.isDone()) {
                return;
            }
            //通知超时
            notifyTimeout(future);
        }


        /**
         * 通知超时
         *
         * @param future 超时通知
         */
        private void notifyTimeout(DefaultFuture future) {
            // 处理结果集
            DefaultFuture.timoutReceived(future.requestSyncMessage.getMessageId(), future.clientChannel);
        }

    }


    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return super.get();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return super.get(timeout, unit);
    }

    @Override
    public Object getNow(Object valueIfAbsent) {
        return super.getNow(valueIfAbsent);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return super.cancel(mayInterruptIfRunning);
    }

    /**
     * 超时相关的通知
     *
     * @param messageId 消息的Id
     * @param channel   消息管道
     */
    public static void timoutReceived(String messageId, Channel channel) {
        if (messageId != null && messageId.length() > 0) {
            DefaultFuture defaultFuture = FUTURE_CACHE.remove(messageId);
            if (defaultFuture != null) {
                defaultFuture.doReceived(new RuntimeException(String.format("request timeout. address is %s.", channel != null ? channel.remoteAddress() : "")));
                defaultFuture.timeoutCheckTask.cancel();
            }
        } else {
            System.err.println("message id is null.");
        }
    }

    /**
     * 通知完成
     *
     * @param syncMessage 消息结果集
     */
    public static void received(SyncMessage syncMessage, Channel channel) {
        String messageId = syncMessage.getMessageId();
        if (messageId != null && messageId.length() > 0) {
            DefaultFuture defaultFuture = FUTURE_CACHE.remove(messageId);
            if (defaultFuture != null) {
                defaultFuture.doReceived(syncMessage);
                defaultFuture.timeoutCheckTask.cancel();
            } else {
                String warnMessage = "The timeout response finally returned at %s , response status is false %s , please check provider side for detailed result.";
                String message = String.format(warnMessage, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()),
                        channel == null ? "" : ", channel: " + channel.remoteAddress());
                System.err.println(message);
            }
        } else {
            System.err.println("message id is null.");
        }
    }

    public void doReceived(Object result) {
        complete(result);
    }
}
