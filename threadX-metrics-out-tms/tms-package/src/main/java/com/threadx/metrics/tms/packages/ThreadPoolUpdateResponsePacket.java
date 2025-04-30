package com.threadx.metrics.tms.packages;

import com.threadx.communication.common.agreement.packet.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 同步消息响应器
 *
 * @author huangfukexing
 * @date 2023/8/10 16:48
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolUpdateResponsePacket extends Message {
    private static final long serialVersionUID = 5136484031973224557L;
}
