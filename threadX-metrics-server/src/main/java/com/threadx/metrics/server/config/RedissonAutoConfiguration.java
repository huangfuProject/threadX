package com.threadx.metrics.server.config;

import cn.hutool.core.collection.CollUtil;
import com.threadx.metrics.server.config.properties.RedissonProperties;
import com.threadx.metrics.server.lock.RedisDistributedLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * redis分布式锁配置类
 * RedisProperties
 * @author huangfukexing
 * @date 2023/5/6 14:16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.lock", name = "model", havingValue = "simple")
    public Config simpleConfig(RedisProperties redisProperties, RedissonProperties redissonProperties){
        Config config = new Config();

        String host = redisProperties.getHost();
        int port = redisProperties.getPort();
        String username = redisProperties.getUsername();
        String password = redisProperties.getPassword();
        Duration connectTimeout = redisProperties.getConnectTimeout();
        int database = redisProperties.getDatabase();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(String.format("redis://%s:%d", host, port));
        singleServerConfig.setDatabase(database);
        singleServerConfig.setUsername(username);
        singleServerConfig.setPassword(password);
        if(connectTimeout != null) {
            singleServerConfig.setConnectTimeout((int) connectTimeout.toMillis());
        }
        return config;
    }


    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.lock", name = "model", havingValue = "sentinel")
    public Config sentinelConfig(RedisProperties redisProperties, RedissonProperties redissonProperties){
        Config config = new Config();
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        int database = redisProperties.getDatabase();
        String password = redisProperties.getPassword();
        String userName = redisProperties.getUsername();
        String master = sentinel.getMaster();
        String sentinelPassword = sentinel.getPassword();
        List<String> nodes = sentinel.getNodes();
        SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
        sentinelServersConfig.setDatabase(database);
        sentinelServersConfig.setMasterName(master);
        sentinelServersConfig.setPassword(password);
        sentinelServersConfig.setSentinelPassword(sentinelPassword);
        sentinelServersConfig.setUsername(userName);
        if(CollUtil.isNotEmpty(nodes)) {
            nodes.forEach(node -> sentinelServersConfig.addSentinelAddress(String.format("redis://%s", node)));
        }
        return config;
    }


    @Bean
    @ConditionalOnProperty(prefix = "spring.redis.lock", name = "model", havingValue = "cluster")
    public Config clusterConfig(RedisProperties redisProperties, RedissonProperties redissonProperties){
        Config config = new Config();
        ClusterServersConfig clusterServersConfig = config.useClusterServers();
        RedisProperties.Cluster cluster = redisProperties.getCluster();
        List<String> nodes = cluster.getNodes();
        if(CollUtil.isNotEmpty(nodes)) {
            nodes.forEach(clusterServersConfig::addNodeAddress);
        }
        return config;
    }


    /**
     * 构建Redisson客户端
     * 内联 {@link RedissonClient#shutdown()}
     * 内联 {@link Config} 集群、单机、主从、哨兵的配置关键字，均可在该类查看
     *
     * @return redisson操作的客户端
     * @throws IOException io异常
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnBean(Config.class)
    public RedissonClient redissonClient(Config config) throws IOException {
        log.info("create RedissonClient, config is : {}", config.toYAML());
        return Redisson.create(config);
    }



    /**
     * 构建Redisson客户端
     *
     * @param redissonClient 客户端
     * @return redisson操作的客户端
     * @throws IOException io异常
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient.class)
    public RedisDistributedLockTemplate distributedLockTemplate(RedissonClient redissonClient) throws IOException {
        return new RedisDistributedLockTemplate(redissonClient);
    }
}
