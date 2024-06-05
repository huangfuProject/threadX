package com.threadx.metrics.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson分布式锁配置项
 *
 * @author huangfukexing
 * @date 2023/5/6 14:47
 */
@Data
@ConfigurationProperties(prefix = "spring.redis.lock")
public class RedissonProperties {
    /**
     * 分布式锁的模型，可选值为  simple sentinel cluster
     */
    private String model;
}
