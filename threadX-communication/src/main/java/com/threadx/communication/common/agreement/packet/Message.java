package com.threadx.communication.common.agreement.packet;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息定义的父类
 *
 * @author huangfu
 * @date 2023/4/7 09:19
 */
@Data
public abstract class Message implements Serializable {

    private static final long serialVersionUID = -60259995547914226L;

    private static final AtomicLong IDX = new AtomicLong();

    public Message() {
        this.messageId = IDX.incrementAndGet();
    }

    /**
     * 是否是同步信息
     */
    private boolean sync = false;

    /**
     * 消息的id
     */
    private final Long messageId;

    /**
     * 开启异步
     */
    public void enableAsync() {
        this.sync = false;
    }

    /**
     * 开启同步
     */
    public void enableSync() {
        this.sync = true;
    }


    /**
     * 获取当前类的类型
     *
     * @return 返回具体实现的类的全限定名
     */
    public final String classType() {
        return this.getClass().getName();
    }

}
