package com.threadx.communication.common;


import com.threadx.communication.common.agreement.AgreementChoreography;
import com.threadx.communication.common.agreement.DefaultAgreementChoreography;
import com.threadx.communication.common.agreement.packet.Message;
import com.threadx.communication.common.serializes.MessageSerialize;
import com.threadx.communication.common.serializes.ProtostuffSerialize;

/**
 * *************************************************<br/>
 * 消息处理过程中出现的处理器等操作的暂存区<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2022/10/9 9:39
 */
public class DefaultMessageCommunicationConfig implements MessageCommunicationConfig {
    /**
     * 网络数据包的编解码处理程序
     */
    private AgreementChoreography agreementChoreography = new DefaultAgreementChoreography();

    /**
     * 对象序列化和反序列化的工具包
     */
    private MessageSerialize<Message> messageSerialize = new ProtostuffSerialize<>();

    @Override
    public AgreementChoreography getAgreementChoreography() {
        return agreementChoreography;
    }

    @Override
    public void setAgreementChoreography(AgreementChoreography agreementChoreography) {
        this.agreementChoreography = agreementChoreography;
    }

    @Override
    public MessageSerialize<Message> getMessageSerialize() {
        return messageSerialize;
    }

    @Override
    public void setMessageSerialize(MessageSerialize<Message> messageSerialize) {
        this.messageSerialize = messageSerialize;
    }
}
