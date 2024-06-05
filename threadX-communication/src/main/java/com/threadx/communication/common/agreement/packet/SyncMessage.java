package com.threadx.communication.common.agreement.packet;

import lombok.*;

import java.util.UUID;

/**
 * 同步消息
 *
 * @author huangfukexing
 * @date 2023/8/16 17:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SyncMessage extends Message{
    private static final long serialVersionUID = 2418733157371766241L;

    public SyncMessage() {
        this.messageId = UUID.randomUUID().toString().replace("-","");
    }

    /**
     * 消息的id  当消息的id不为空的时候 默认为同步请求
     */
    private String messageId;


    /**
     * 是否成功
     */
    private boolean success = false;

    /**
     * 错误信息
     */
    private String errorMessage;
}
