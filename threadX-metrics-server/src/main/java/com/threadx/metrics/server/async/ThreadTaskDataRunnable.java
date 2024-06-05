package com.threadx.metrics.server.async;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.threadx.communication.common.agreement.packet.ThreadPoolTaskCollectMessage;
import com.threadx.metrics.server.constant.RedisKeyConstant;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.init.ThreadTaskDataConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 线程池任务数据异步处理器
 *
 * @author huangfukexing
 * @date 2023/4/18 12:42
 */
@Slf4j
public class ThreadTaskDataRunnable implements Runnable {

    /**
     * 线程池收集的数据
     */
    private final ThreadPoolTaskCollectMessage threadPoolTaskCollectMessage;
    /**
     * 数据来源
     */
    private final String ipaddress;

    public ThreadTaskDataRunnable(ThreadPoolTaskCollectMessage threadPoolTaskCollectMessage, String ipaddress) {
        this.threadPoolTaskCollectMessage = threadPoolTaskCollectMessage;
        this.ipaddress = ipaddress;
    }

    @Override
    public void run() {
        ThreadTaskData threadTaskData = new ThreadTaskData();
        threadTaskData.init();
        BeanUtil.copyProperties(threadPoolTaskCollectMessage, threadTaskData);
        threadTaskData.setAddress(ipaddress);
        threadTaskData.setThreadPoolObjectId(threadPoolTaskCollectMessage.getThreadPoolId());
        ThreadTaskDataConsumer.pushData(threadTaskData);
    }
}
