package com.threadx.communication.common.agreement.implementation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 网路传输的数据包的默认拆解程序
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
public class DefaultPacketSegmentationHandler extends LengthFieldBasedFrameDecoder implements PacketSegmentationHandler {

    /**
     * 协议头的长度
     */
    private final static Integer PROTOCOL_LENGTH = 2;
    /**
     * 标识数据体长度的字节位数
     */
    private final static Integer DATA_LENGTH = 4;

    /**
     * 魔数长度
     */
    public static final int MAGIC_COUNT = 2;


    public DefaultPacketSegmentationHandler() {
        super(Integer.MAX_VALUE, PROTOCOL_LENGTH, DATA_LENGTH);
    }

    @Override
    public ByteBuf segmentation(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //如果可读字节小于魔数长度  证明包不完整  返回一个空
        if (in.readableBytes() < MAGIC_COUNT) {
            return null;
        }

        //从数据包的头部获取一个魔数    不是指定数字的 一律过滤掉
        if (in.getInt(in.readerIndex()) != DefaultMessageAgreementLayout.MAGIC) {
            ctx.channel().closeFuture().sync();
            return null;
        }

        return (ByteBuf)super.decode(ctx, in);
    }
}
