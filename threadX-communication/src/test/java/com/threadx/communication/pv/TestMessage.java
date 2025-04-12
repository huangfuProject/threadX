package com.threadx.communication.pv;

import com.threadx.communication.common.agreement.packet.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 测试消息
 *
 * @author huangfukexing
 * @date 2025/4/12 21:04
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestMessage extends Message {
    private static final long serialVersionUID = -8766162438253425986L;

    private String name;
}
