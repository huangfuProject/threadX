package com.threadx.communication.client;

import com.threadx.communication.client.config.ClientConfig;
import com.threadx.communication.client.handler.ClientHeartbeatHandler;
import com.threadx.communication.common.MessageCommunicationConfig;
import com.threadx.communication.common.agreement.AgreementChoreography;
import com.threadx.communication.common.agreement.implementation.PacketSegmentationHandler;
import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.handlers.PacketCodecHandler;
import com.threadx.communication.common.utils.NettyEventLoopUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 客户端
 *
 * @author huangfukexing
 * @date 2023/4/7 14:31
 */
@EqualsAndHashCode
public class CommunicationClient {

    static final Logger logger = Logger.getGlobal();

    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;

    private final static int DEFAULT_IO_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    private EventLoopGroup eventLoopGroup = null;

    private final AtomicBoolean active = new AtomicBoolean(false);

    /**
     * 启动器
     */
    private Bootstrap bootstrap;

    /**
     * 通讯管道
     */
    private Channel channel;

    /**
     * 配置信息
     */
    private final ClientConfig clientConfig;

    public CommunicationClient(ClientConfig clientConfig) {
        ConnectionManager.reConnectionConfinementConnection();
        this.clientConfig = clientConfig;
        //连接服务器
        connect();
    }

    /**
     * 重新连接
     */
    public void reConnect() {
        this.close();
        connect();
    }

    /**
     * 连接服务器
     */
    private void connect() {
        eventLoopGroup = NettyEventLoopUtils.eventLoopGroup(DEFAULT_IO_THREADS, "threadX-Client-Worker");
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //设置通道选项 SO_KEEPALIVE 为 true，表示启用 TCP 的 keepalive 机制，即使长时间没有数据传输也能保持连接。
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                //设置通道选项 TCP_NODELAY 为 true，表示禁用 Nagle 算法，可以降低延迟但会增加网络负载。
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                //设置通道选项 ALLOCATOR 为 Netty 提供的对象池化的字节缓冲区分配器，可以减少内存分配和垃圾回收的开销，提高性能。
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                //设置通道类型为 NettyEventLoopUtils 工具类提供的套接字通道类型，该类型会根据操作系统的不同选择合适的实现。
                .channel(NettyEventLoopUtils.socketChannelClass())
                .remoteAddress(clientConfig.getHost(), clientConfig.getPort());
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_CONNECT_TIMEOUT);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) {
                //获取消息协议上下文
                MessageCommunicationConfig communicationConfig = clientConfig.getMessageCommunicationConfig();

                //获取网络数据包分割器
                AgreementChoreography agreementChoreography = communicationConfig.getAgreementChoreography();
                PacketSegmentationHandler packetSegmentationHandler = agreementChoreography.segmentationHandler();

                //数据通讯管道编排
                //写入数据包分割器  能区分一个完整的包数据
                socketChannel.pipeline().addLast("PacketSegmentationHandler", packetSegmentationHandler);
                //写入数据编解码器  将一个完整的包数据进行包编解码
                socketChannel.pipeline().addLast("PacketCodecHandler", new PacketCodecHandler(communicationConfig));
                Map<String, ChannelInboundHandlerAdapter> channelInboundHandlerAdapterMap = clientConfig.getChannelInboundHandlerAdapterMap();
                //消息处理器
                channelInboundHandlerAdapterMap.forEach((handlerName,handler) -> socketChannel.pipeline().addLast(handlerName, handler));
                //写入心跳实现
                socketChannel.pipeline().addLast("ClientHeartbeatHandler", new ClientHeartbeatHandler(CommunicationClient.this));
            }
        });

        //开始连接服务器
        bootstrap.connect().addListener((ChannelFutureListener) cf -> {
            if (cf.isSuccess()) {
                channel = cf.channel();
                ConnectionManager.newActiveConnection(CommunicationClient.this);
                active.compareAndSet(false, true);
                logger.info("Connecting to the server succeeded. Procedure ：" + getServerAddress());
            } else {
                //写入坏连接  等待重连
                ConnectionManager.confinementConnection(CommunicationClient.this);
                active.compareAndSet(true, false);
                logger.log(Level.WARNING, getServerAddress() + "Connection failed. Bad connection was added. Procedure", cf.cause());
            }
        });

    }


    /**
     * 异步的发送一个消息
     *
     * @param message 消息体
     */
    public void asyncSendMessage(Message message) {
        message.setServerKey(clientConfig.getServerKey());
        message.setInstanceKey(clientConfig.getInstanceKey());
        channel.writeAndFlush(message);
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
     */
    public void close() {
        try {
            if (bootstrap != null) {
                eventLoopGroup.shutdownGracefully();
            }
            bootstrap = null;
        }finally {
            active.compareAndSet(true, false);
        }


    }

    /**
     * 将过期的连接放到小黑屋
     */
    public void failureThisConnection() {
        //失效终端
        ConnectionManager.confinementConnection(this);
    }

    /**
     * 判断通道是否活跃
     *
     * @return 是否活跃
     */
    public boolean communicationStatus() {
        return channel.isOpen() && channel.isActive() && active.get();
    }

    public String getServerAddress() {
        return String.format("%s:%s",clientConfig.getHost(), clientConfig.getPort());
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }
}
