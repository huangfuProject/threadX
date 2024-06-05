package com.threadx.metrics.server.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mysql.cj.util.LogUtils;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.entity.InstanceItem;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ThreadTaskDataService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * *************************************************<br/>
 * 线程池数据消费者<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/4/22 15:25
 */
@Slf4j
@Component("threadTaskDataConsumer")
public class ThreadTaskDataConsumer implements InitializingBean, DisposableBean {

    private final static String QUEUE_SIZE = System.getProperties().getProperty("thread.task.data.queue.size", "4096");
    private final static MpscArrayQueue<ThreadTaskData> THREAD_POOL_DATA = new MpscArrayQueue<>(Integer.parseInt(QUEUE_SIZE));
    private final static AtomicBoolean IS_START = new AtomicBoolean(false);
    private final static ScheduledThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("ThreadTaskDataConsumer");
        return thread;
    });

    private final ThreadTaskDataService threadTaskDataService;

    private ScheduledFuture<?> scheduledFuture = null;

    public ThreadTaskDataConsumer(ThreadTaskDataService threadTaskDataService) {
        this.threadTaskDataService = threadTaskDataService;
    }


    @Override
    public void destroy() throws Exception {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        THREAD_POOL_DATA.clear();
        IS_START.compareAndSet(false, true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IS_START.compareAndSet(false, true);
        scheduledFuture = THREAD_POOL.scheduleAtFixedRate(() -> {
            try {
                List<ThreadTaskData> elements = new ArrayList<>();
                MessagePassingQueue.Consumer<ThreadTaskData> consumer = elements::add;
                THREAD_POOL_DATA.drain(consumer, 50);
                //批量入库
                if (CollUtil.isNotEmpty(elements)) {
                    threadTaskDataService.batchSave(elements);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 2, TimeUnit.SECONDS);
    }

    public static void pushData(ThreadTaskData threadTaskData) {
        if (IS_START.get()) {
            String serverKey = threadTaskData.getServerKey();
            String instanceKey = threadTaskData.getInstanceKey();
            InstanceItemService instanceItemService = SpringUtil.getBean(InstanceItemService.class);
            threadTaskData.setInstanceId(instanceItemService.findByInstanceNameAndServerNameOrCreateOnCache(serverKey, instanceKey));
            if (!THREAD_POOL_DATA.offer(threadTaskData)) {
                log.warn("Existential task dropping {}, Please set the parameter -Dthread.task.data.queue.size=value(value greater than {})", JSONUtil.toJsonStr(threadTaskData), QUEUE_SIZE);
            }
        } else {
            log.error("com.threadx.metrics.server.init.ThreadPoolDataConsumer spring bean Be destroyed.");
        }
    }
}
