package com.threadx.communication.common.agreement.implementation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * 数据包分割器
 * <p>
 * 这里主要用于将一个混乱的繁杂的网络数据包切分为一个或者多个数据包的完整的数据包的处理程序
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
public interface PacketSegmentationHandler extends ChannelHandler {

    /**
     * 数据切分  将数据切分为一个完整的数据包返回回来
     *
     * @param ctx 管道程序处理上下文
     * @param in  没有经过拆解的原始的网络数据包
     * @return 返回一个拆解好的数据包，当返回为null的时候证明这个拆解没有拆分出来，跳过拆分
     * @throws Exception 异常信息
     */
    ByteBuf segmentation(ChannelHandlerContext ctx, ByteBuf in) throws Exception;
}
