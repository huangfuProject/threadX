package com.threadx.communication.common.utils;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Netty各类事件选择器生成的工具类
 *
 * @author huangfu
 * @date 2022年10月10日09:32:04
 */
public class NettyEventLoopUtils {

    /**
     * 根据当前环境信息生成一个EventLoopGroup
     *
     * @param threads           线程池
     * @param threadFactoryName 线程名称
     * @return 事件循环
     */
    public static EventLoopGroup eventLoopGroup(int threads, String threadFactoryName) {
        ThreadFactory threadFactory = new DefaultThreadFactory(threadFactoryName, true);
        return shouldEpoll() ? new EpollEventLoopGroup(threads, threadFactory) : new NioEventLoopGroup(threads, threadFactory);
    }

    /**
     * socket通道的类型 client
     *
     * @return socket通道的类型
     */
    public static Class<? extends SocketChannel> socketChannelClass() {
        return shouldEpoll() ? EpollSocketChannel.class : NioSocketChannel.class;
    }

    /**
     * socket通道的类型 server
     *
     * @return socket通道的类型
     */
    public static Class<? extends ServerSocketChannel> serverSocketChannelClass() {
        return shouldEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    /**
     * 判断是不是使用epoll模型
     *
     * @return 是不是linux
     */
    private static boolean shouldEpoll() {
        String isEpollEnable = System.getProperty("netty.epoll.enable");
        if (StrUtil.isBlank(isEpollEnable)) {
            isEpollEnable = "false";
        }
        if (Boolean.parseBoolean(isEpollEnable)) {
            String osName = System.getProperty("os.name");
            return osName.toLowerCase().contains("linux") && Epoll.isAvailable();
        }

        return false;
    }
}
