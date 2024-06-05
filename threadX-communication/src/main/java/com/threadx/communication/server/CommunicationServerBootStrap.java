package com.threadx.communication.server;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.threadx.communication.common.MessageCommunicationConfig;
import com.threadx.communication.common.agreement.AgreementChoreography;
import com.threadx.communication.common.agreement.implementation.PacketSegmentationHandler;
import com.threadx.communication.common.handlers.PacketCodecHandler;
import com.threadx.communication.common.utils.NettyEventLoopUtils;
import com.threadx.communication.server.config.ServerConfig;
import com.threadx.communication.server.handler.ServerHeartbeatHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author huangfukexing
 * @date 2023/4/7 13:14
 */
public class CommunicationServerBootStrap {

    static final Logger logger = Logger.getGlobal();
    /**
     * 服务端的默认工作线程池
     */
    private final static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    /**
     * 分发管理器
     */
    private EventLoopGroup bossGroup;
    /**
     * 工作管理器
     */
    private EventLoopGroup workerGroup;

    /**
     * 启动引导器
     */
    private ServerBootstrap bootstrap;
    /**
     * 服务管道
     */
    private Channel channel;

    /**
     * 启动配置项
     */
    private final ServerConfig serverConfig;

    public CommunicationServerBootStrap(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    /**
     * 启动服务
     */
    public void startServer() {
        logger.info("threadX指标收集服务开始启动！");
        MessageCommunicationConfig messageCommunicationConfig = serverConfig.getMessageCommunicationConfig();
        Map<String, ChannelInboundHandlerAdapter> channelInboundHandlerAdapterMap = serverConfig.getChannelInboundHandlerAdapterMap();
        bossGroup = NettyEventLoopUtils.eventLoopGroup(1, "threadX-server-boss");
        workerGroup = NettyEventLoopUtils.eventLoopGroup(DEFAULT_IO_THREADS, "threadX-server-worker");

        bootstrap = new ServerBootstrap();
        //初始化netty解码器
        bootstrap.group(bossGroup, workerGroup)
                .channel(NettyEventLoopUtils.serverSocketChannelClass())
                //启用了SO_REUSEADDR套接字选项，它允许一个处于TIME_WAIT状态的socket重新绑定到一个已经使用过的本地地址和端口。
                // 这对于服务器端口快速重启和避免"Address already in use"错误非常有用。
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                //启用了TCP_NODELAY套接字选项，它禁用了Nagle算法，即数据在发送前不缓冲，可以降低延迟，提高实时性。
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                //禁用了SO_KEEPALIVE套接字选项，这个选项会定期发送心跳包以检测连接是否已经断开。
                // 通常对于长连接是必须的，但对于短连接或连接数目很多的情况，禁用SO_KEEPALIVE可以减少网络流量和系统资源的占用。
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.FALSE)
                //指定了一个用于内存分配的分配器，这里使用了Netty自带的PooledByteBufAllocator。
                // 这个分配器可以重用ByteBuf，避免了频繁的内存分配和释放，减少了内存碎片。
                // 同时，由于Netty的ByteBuf支持池化，所以可以提高性能。
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //获取网络数据包分割器
                        AgreementChoreography agreementChoreography = messageCommunicationConfig.getAgreementChoreography();
                        PacketSegmentationHandler packetSegmentationHandler = agreementChoreography.segmentationHandler();
                        //数据通讯管道编排
                        //写入数据包分割器  能区分一个完整的包数据
                        socketChannel.pipeline().addLast("PacketSegmentationHandler", packetSegmentationHandler);
                        //写入数据编解码器 将一个完整的包数据进行包编解码
                        socketChannel.pipeline().addLast("PacketCodecHandler", new PacketCodecHandler(messageCommunicationConfig));
                        //添加处理器
                        if(CollUtil.isNotEmpty(channelInboundHandlerAdapterMap)) {
                            channelInboundHandlerAdapterMap.forEach((k,v) -> socketChannel.pipeline().addLast(k, v));
                        }
                        //写入心跳实现
                        socketChannel.pipeline().addLast("ServerHeartbeatHandler", new ServerHeartbeatHandler());
                    }
                });
        //获取主机
        String host = serverConfig.getHost();
        Integer port = serverConfig.getPort();

        InetSocketAddress bindAddress;
        if (StrUtil.isNotBlank(host)) {
            bindAddress = new InetSocketAddress(host, port);
        } else {
            bindAddress = new InetSocketAddress(port);
        }
        ChannelFuture channelFuture = bootstrap.bind(bindAddress);
        //等待绑定完成
        channelFuture.syncUninterruptibly();
        channel = channelFuture.channel();
        logger.info("服务启动成功：" + channel.localAddress());
    }


    /**
     * 等到服务端关闭
     *
     * @throws InterruptedException 中断异常
     */
    public void await() throws InterruptedException {
        channel.closeFuture().sync();
    }

    /**
     * 关闭服务端
     *
     * @throws Exception 异常信息
     */
    public void close() throws Exception {
        if (bootstrap != null) {
            bossGroup.shutdownGracefully().syncUninterruptibly();
            workerGroup.shutdownGracefully().syncUninterruptibly();
        }
    }
}
