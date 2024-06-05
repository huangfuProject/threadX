package com.threadx.metrics.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 实例信息
 *
 * @author huangfukexing
 * @date 2023/5/5 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InstanceItem extends BaseEntity {
    private static final long serialVersionUID = -7647930126073288339L;

    /**
     * 实例名称
     */
    private String instanceName;

    /**
     * 关联的服务id
     */
    private Long serverId;

    /**
     * 服务的名称
     */
    private String serverName;

    /**
     * 活跃时间
     */
    private Long activeTime;
}
