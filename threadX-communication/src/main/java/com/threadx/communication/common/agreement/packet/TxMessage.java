package com.threadx.communication.common.agreement.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 请求相关的参数
 *
 * @author huangfukexing
 * @date 2025/4/12 16:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TxMessage extends Message{
    private static final long serialVersionUID = -925462857060148230L;

    /**
     *  参数信息
     */
    private Object param;


}
