package com.threadx.communication.common.agreement.packet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息定义的父类
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = -60259995547914226L;




    /**
     * 服务的名称
     */
    private String serverKey;
    /**
     * 实例名称
     */
    private String instanceKey;

    /**
     * 获取当前类的类型
     *
     * @return 返回具体实现的类的全限定名
     */
    public final String classType() {
        return this.getClass().getName();
    }

}
