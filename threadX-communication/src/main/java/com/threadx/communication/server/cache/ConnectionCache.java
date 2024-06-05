package com.threadx.communication.server.cache;

import com.threadx.communication.common.utils.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 连接缓存
 *
 * @author huangfukexing
 * @date 2023/8/24 15:47
 */
public class ConnectionCache {

    private final static Integer TIMEOUT_CONNECTION = 120;

    private static final Map<String, ChannelHandlerContextTime> CONNECTION_CACHE = new ConcurrentHashMap<>();

    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("threadX-communication-server-connect-check");
            return thread;
        }
    });

    static {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            List<String> timeoutKeys = new ArrayList<>();
            CONNECTION_CACHE.forEach((k, v) -> {
                Long addTime = v.getAddTime();
                if ((System.currentTimeMillis() - addTime) > TimeUnit.SECONDS.toMillis(TIMEOUT_CONNECTION)) {
                    timeoutKeys.add(k);
                }
            });
            timeoutKeys.forEach(CONNECTION_CACHE::remove);
        }, 10, 5, TimeUnit.SECONDS);
    }

    /**
     * 追加一个连接
     *
     * @param ctx 管理中心
     */
    public static void addConnection(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = ChannelUtil.getChannelRemoteAddress(channel);
        CONNECTION_CACHE.put(address, new ChannelHandlerContextTime(ctx));
    }

    /**
     * 是否存活  true  活跃  false  没有
     *
     * @param address 地址
     * @return 是否存活
     */
    public static boolean isActive(String address) {
        return getConnection(address) != null;
    }

    /**
     * 根据地址获取一个连接
     *
     * @param address 地址信息
     * @return 连接上下文
     */
    public static ChannelHandlerContext getConnection(String address) {
        ChannelHandlerContextTime channelHandlerContextTime = CONNECTION_CACHE.get(address);
        if (channelHandlerContextTime != null) {
            Long addTime = channelHandlerContextTime.getAddTime();
            ChannelHandlerContext channelHandlerContext = channelHandlerContextTime.getChannelHandlerContext();

            if ((System.currentTimeMillis() - addTime) > TimeUnit.SECONDS.toMillis(TIMEOUT_CONNECTION)) {
                removeConnection(address);
                return null;
            }

            return channelHandlerContext;
        }
        return null;
    }

    /**
     * 删除一个连接
     *
     * @param ctx 要删除的地址
     */
    public static void removeConnection(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String address = ChannelUtil.getChannelRemoteAddress(channel);
        CONNECTION_CACHE.remove(address);
    }

    /**
     * 删除一个连接
     *
     * @param address 要删除的地址
     */
    public static void removeConnection(String address) {
        CONNECTION_CACHE.remove(address);
    }


    static class ChannelHandlerContextTime {
        /**
         * 追加时间
         */
        private Long addTime;

        /**
         * 通讯管道
         */
        private ChannelHandlerContext channelHandlerContext;

        public ChannelHandlerContextTime(ChannelHandlerContext channelHandlerContext) {
            this(channelHandlerContext, System.currentTimeMillis());
        }

        public ChannelHandlerContextTime(ChannelHandlerContext channelHandlerContext, Long addTime) {
            this.addTime = addTime;
            this.channelHandlerContext = channelHandlerContext;
        }

        public Long getAddTime() {
            return addTime;
        }

        public void setAddTime(Long addTime) {
            this.addTime = addTime;
        }

        public ChannelHandlerContext getChannelHandlerContext() {
            return channelHandlerContext;
        }

        public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
            this.channelHandlerContext = channelHandlerContext;
        }
    }
}
