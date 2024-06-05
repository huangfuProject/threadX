package com.threadx.communication.common.agreement.implementation;

import io.netty.buffer.ByteBuf;

/**
 * *************************************************<br/>
 * 消息协议定义<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
public interface MessageAgreementLayout {

    /**
     * 消息编码
     *
     * @param data 数据包
     * @return 编码成功后的数据
     */
    ByteBuf messageEncode(byte[] data);


    /**
     * 消息解码
     *
     * @param byteBuf 完整的消息体
     * @return 消息解码后的数据包
     */
    byte[] messageDecode(ByteBuf byteBuf);
}
