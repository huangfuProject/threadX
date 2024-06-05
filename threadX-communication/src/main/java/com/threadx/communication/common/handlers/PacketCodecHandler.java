package com.threadx.communication.common.handlers;

import com.threadx.communication.common.MessageCommunicationConfig;
import com.threadx.communication.common.agreement.AgreementChoreography;
import com.threadx.communication.common.agreement.implementation.MessageAgreementLayout;
import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.serializes.MessageSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * 协议包编码：<br/>
 * 协议包编码需要以下几个步骤<br/>
 * <p>
 * 1. 基于获取到的序列化协议处理器将传输过来的对象进行编码操作，然后将数据对象序列化为字节数组
 * 2. 将获取到的请求体字节数组基于获取到的协议框架进行编码，调用{@link MessageAgreementLayout#messageEncode(byte[])}进行编码为预设协议体，并传输到网络传输的数据包中
 * <p>
 * 协议包解码：<br/>
 * 协议包解码需要以下几个步骤<br/>
 * <p>
 * 1. 基于获取到的协议编排处理器我们可以知道本次处理的包结构内部协议体是如何设计的，我们知道了协议体是如何编排的，我们就能通过回调 {@link MessageAgreementLayout#messageDecode(ByteBuf)} 进行请求体的解析！<br/>
 * 2. 上一步操作获取到了请求体的字节数组，我们就能够基于获取到的序列化处理器，将该字节数组反序列化为一个完整的对象向下传递！<br/>
 * <p>
 *
 * @author huangfu
 * @date 2022年10月11日09:54:03
 */
public class PacketCodecHandler extends ByteToMessageCodec<Message> {
    /**
     * 网络传输协议中的协议的编排处理器
     */
    private final MessageAgreementLayout messageAgreementLayout;
    /**
     * 对象序列化工具对象
     */
    private final MessageSerialize<Message> messageSerialize;

    public PacketCodecHandler(MessageCommunicationConfig config) {
        //获取 协议编排 选择器
        AgreementChoreography agreementChoreography = config.getAgreementChoreography();
        //获取协议编排处理器
        messageAgreementLayout = agreementChoreography.messageAgreementLayout();
        //获取序列化协议
        messageSerialize = config.getMessageSerialize();
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {
        //先将对象序列化
        byte[] bytes = messageSerialize.serializeObject(message);
        //将序列化好的字节数组
        ByteBuf encodeByteBuf = messageAgreementLayout.messageEncode(bytes);
        //将数据编码为具体的协议体框架后 写入网络传输包
        byteBuf.writeBytes(encodeByteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            //获取数据包体
            byte[] bytes = messageAgreementLayout.messageDecode(in);
            //对象反序列化
            Message message = messageSerialize.objectDeserialize(bytes, Message.class);
            out.add(message);
        } catch (Exception e) {
            ctx.fireExceptionCaught(e);
        }

    }
}