package com.threadx.metrics.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 服务信息
 *
 * @author huangfukexing
 * @date 2023/5/5 14:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServerItem extends BaseEntity {
    private static final long serialVersionUID = -5487722248934377755L;

    /**
     * 服务名称
     */
    private String serverName;
}
