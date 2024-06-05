package com.threadx.metrics.server.entity;

import com.threadx.metrics.server.dto.ThreadPoolVariableParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池修改日志
 *
 * @author huangfukexing
 * @date 2023/9/18 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolUpdateLog implements Serializable {
    private static final long serialVersionUID = -5723006871973352929L;

    /**
     * 线程池id
     */
    private Long threadPoolId;

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 实例的名称
     */
    private String instanceName;

    /**
     * 实例的id
     */
    private Long instanceId;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 修改的用户名
     */
    private String updateNickName;

    /**
     * 修改时间
     */
    private String updateDate;

    /**
     * 修改时间
     */
    private String resultMessage;

    /**
     * 修改的详情
     */
    private String details;

    public void formatUpdateDetails(ThreadPoolVariableParameter threadPoolVariableParameter){
        this.details = String.format("并发数量:%s\n " +
                        "最大并发:%s\n " +
                        "空闲时间:%s毫秒\n" +
                        " 拒绝策略:%s",
                threadPoolVariableParameter.getCoreSize(), threadPoolVariableParameter.getMaximumPoolSize(),
                threadPoolVariableParameter.getKeepAliveTime(), threadPoolVariableParameter.getRejectedExecutionHandlerClass());
    }
}
