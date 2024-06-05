package com.threadx.communication.common.agreement.packet;

import lombok.*;

import java.io.Serializable;

/**
 * 心跳消息
 *
 * @author huangfukexing
 * @date 2023/4/25 13:16
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeartbeatMessage extends Message implements Serializable {
    private static final long serialVersionUID = -4126696013722626261L;
    /**
     * 发送时间
     */
    private final long sendTime;

    public HeartbeatMessage() {
        this.sendTime = System.currentTimeMillis();
    }
}
