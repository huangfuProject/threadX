package com.threadx.communication.common.agreement.packet;

import lombok.*;

/**
 * 同步消息发送相应器
 *
 * @author huangfukexing
 * @date 2023/8/10 16:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolUpdateResponseMessage extends SyncMessage {
    private static final long serialVersionUID = 5136484031973224557L;
}
