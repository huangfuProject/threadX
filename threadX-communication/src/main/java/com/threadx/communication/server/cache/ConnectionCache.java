package com.threadx.communication.server.cache;


import com.threadx.communication.common.load.ThreadXLoadHandler;
import com.threadx.communication.common.utils.ChannelUtil;
import com.threadx.communication.server.config.ServerConfig;
import com.threadx.log.Logger;
import com.threadx.log.LoggerSlf4jLog4j2;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接缓存
 *
 * @author huangfukexing
 * @date 2023/8/24 15:47
 */
public class ConnectionCache {

    private static final Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ConnectionCache.class);


    /**
     * 改为可配置参数
     */
    private static final AtomicInteger TIMEOUT_CONNECTION = new AtomicInteger(120);

    private static final ConcurrentHashMap<String, ChannelHandlerContextTime> CONNECTION_CACHE = new ConcurrentHashMap<>(512);

    // 优化线程池配置
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "threadX-communication-server-connect-check");
        t.setDaemon(true); // 设为守护线程防止阻止JVM关闭
        return t;
    });

    static {
        // 固定延迟策略
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            // 分批遍历优化
            Iterator<Map.Entry<String, ChannelHandlerContextTime>> it = CONNECTION_CACHE.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, ChannelHandlerContextTime> entry = it.next();
                if (!entry.getValue().isActive()) {
                    it.remove(); // 使用迭代器避免二次遍历
                    closeChannelSilently(entry.getValue().getChannelHandlerContext(), false);
                }
            }
        }, 10, 5, TimeUnit.SECONDS);
    }

    /**
     * 添加一个链接
     *
     * @param ctx 管道上下文
     */
// 使用computeIfAbsent保证原子性操作
    public static void addConnection(ChannelHandlerContext ctx) {
        String address = ChannelUtil.getChannelRemoteAddress(ctx.channel());
        CONNECTION_CACHE.compute(address, (k, v) -> {
            if (v != null && v.isActive()) {
                v.updateLastUsed();
                return v;
            }
            return new ChannelHandlerContextTime(ctx);
        });
    }

    /**
     * 获取一个链接
     *
     * @param config 配置对象
     * @return 可用链接
     */
    public static ChannelHandlerContext getConnection(ServerConfig config) {
        List<String> activeAddresses = new ArrayList<>();
        // 惰性检测替代递归
        CONNECTION_CACHE.forEach((address, ctxTime) -> {
            if (ctxTime.getChannelHandlerContext().channel().isActive()) {
                activeAddresses.add(address);
            }
        });

        if (activeAddresses.isEmpty()) {
            return null;
        }

        ThreadXLoadHandler loadHandler = config.getLoadHandler();
        String selectedAddress = loadHandler.load(activeAddresses);
        return CONNECTION_CACHE.get(selectedAddress).getChannelHandlerContext();
    }

    /**
     * 新增优雅关闭方法
     *
     * @param ctx 上下文
     */
    public static void closeChannelSilently(ChannelHandlerContext ctx, boolean isRemove) {
        if (ctx != null && ctx.channel().isActive()) {
            ctx.close().addListener(future -> {
                if (!future.isSuccess()) {
                    logger.error("Channel关闭失败: {}", ctx.channel().remoteAddress());
                }
                if (isRemove) {
                    CONNECTION_CACHE.remove(ChannelUtil.getChannelRemoteAddress(ctx.channel()));
                }

            });
        }
    }

    // 增加动态配置方法
    public static void setTimeout(int seconds) {
        TIMEOUT_CONNECTION.set(seconds);
    }

    /**
     * 连接数统计
     *
     * @return 链接数量
     */
    public static int getActiveCount() {
        return CONNECTION_CACHE.values().stream()
                .filter(ChannelHandlerContextTime::isActive)
                .mapToInt(v -> 1).sum();
    }
    @Setter
    @Getter
    static class ChannelHandlerContextTime {
        // 使用volatile保证可见性
        private volatile Long addTime;
        private final ChannelHandlerContext channelHandlerContext;
        // 增加最后一次使用时间
        private volatile Long lastUsedTime = System.currentTimeMillis();

        public ChannelHandlerContextTime(ChannelHandlerContext ctx) {
            this(System.currentTimeMillis(), ctx);
        }

        public ChannelHandlerContextTime(Long addTime, ChannelHandlerContext channelHandlerContext) {
            this.addTime = addTime;
            this.channelHandlerContext = channelHandlerContext;
        }

        public void updateLastUsed() {
            this.lastUsedTime = System.currentTimeMillis();
        }

        public boolean isActive() {
            final long timeoutThreshold = TimeUnit.SECONDS.toMillis(TIMEOUT_CONNECTION.get());
            Channel channel = channelHandlerContext.channel();
            boolean timeout = (System.currentTimeMillis() - lastUsedTime) > timeoutThreshold;
            return channel.isActive() && channel.isOpen() && !timeout;
        }
    }
}
