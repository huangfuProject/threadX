package com.threadx.communication.common;


import com.threadx.communication.common.agreement.AgreementChoreography;
import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.serializes.MessageSerialize;

/**
 * 消息的编解码处理器
 *
 * @author huangfu
 * @date 2022年10月18日12:02:40
 */
public interface MessageCommunicationConfig {

    /**
     * 返回 网络数据包的编解码处理程序
     *
     * @return 网络数据包的编解码处理程序
     */
    AgreementChoreography getAgreementChoreography();

    /**
     * 设置 网络数据包的编解码处理程序
     *
     * @param agreementChoreography 网络数据包的编解码处理程序
     */
    void setAgreementChoreography(AgreementChoreography agreementChoreography);

    /**
     * 返回 对象序列化和反序列化的工具包
     *
     * @return 对象序列化和反序列化的工具包
     */
    MessageSerialize<Message> getMessageSerialize();

    /**
     * 设置 对象序列化和反序列化的工具包
     *
     * @param messageSerialize 对象序列化和反序列化的工具包
     */
    void setMessageSerialize(MessageSerialize<Message> messageSerialize);
}
