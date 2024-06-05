package com.threadx.communication.common.agreement.implementation;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * *************************************************<br/>
 * 默认的协议体<br/>
 * 默认的协议体中，包含一下的格式：
 * 2字节  魔数校验单位
 * 4字节  数据包长度单位
 * &字节  无限位数据位
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
public class DefaultMessageAgreementLayout implements MessageAgreementLayout {
    /**
     * 魔数   用之区分是否是服务内的组件
     * 16进制的short的数据在操作系统内部 java的short类型的数据占用2个字节 也就是16位置
     * 所以 这里需要使用两个byte数组进行存放该数据转换的字节
     */
    public final static short MAGIC = (short) 0xda521;


    @Override
    public ByteBuf messageEncode(byte[] data) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(data.length);
        // 写入数据校验位
        byteBuf.writeShort(MAGIC);
        // 写入字段数据长度
        int dataLength = data.length;
        // 将数据长度写入协议体
        byteBuf.writeInt(dataLength);
        // 写入数据
        byteBuf.writeBytes(data);
        return byteBuf;
    }

    @Override
    public byte[] messageDecode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(Short.BYTES);
        // 获取数据长度
        int dataLength = byteBuf.readInt();
        // 读取数据
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        return data;
    }
}
