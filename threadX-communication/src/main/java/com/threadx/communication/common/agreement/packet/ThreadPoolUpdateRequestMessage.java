package com.threadx.communication.common.agreement.packet;

import lombok.*;

/**
 * 同步消息请求器
 *
 * @author huangfukexing
 * @date 2023/8/10 16:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ThreadPoolUpdateRequestMessage extends SyncMessage {
    private static final long serialVersionUID = 5136484031973224557L;

    /**
     * 线程池的对象的id
     */
    private String objectId;

    /**
     * 线程池核心数量
     */
    private Integer coreSize;



    /**
     * 线程池最大数量
     */
    private Integer maximumPoolSize;

    /**
     * 线程池空闲时间  单位毫秒
     */
    private Long keepAliveTime;

    /**
     * 线程池拒绝策略全限定名
     */
    private String rejectedExecutionHandlerClass;
}
