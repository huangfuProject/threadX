package com.threadx.metrics.server.init;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.entity.ThreadPoolData;
import com.threadx.metrics.server.service.InstanceItemService;
import com.threadx.metrics.server.service.ThreadPoolDataService;
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
@Component("threadPoolDataConsumer")
public class ThreadPoolDataConsumer implements InitializingBean, DisposableBean {
    private final static String QUEUE_SIZE = System.getProperties().getProperty("thread.pool.data.queue.size", "4096");
    private final static MpscArrayQueue<ThreadPoolData> THREAD_POOL_DATA = new MpscArrayQueue<>(Integer.parseInt(QUEUE_SIZE));
    private final static AtomicBoolean IS_START = new AtomicBoolean(false);
    private final static ScheduledThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("ThreadPoolDataConsumer");
        return thread;
    });

    private ScheduledFuture<?> scheduledFuture = null;


    private final ThreadPoolDataService threadPoolDataService;

    public ThreadPoolDataConsumer(ThreadPoolDataService threadPoolDataService) {
        this.threadPoolDataService = threadPoolDataService;
    }

    @Override
    public void destroy() throws Exception {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        THREAD_POOL_DATA.clear();
        IS_START.compareAndSet(true, false);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IS_START.compareAndSet(false, true);
        scheduledFuture = THREAD_POOL.scheduleAtFixedRate(() -> {
            try {
                List<ThreadPoolData> elements = new ArrayList<>();
                MessagePassingQueue.Consumer<ThreadPoolData> consumer = elements::add;
                THREAD_POOL_DATA.drain(consumer, 50);
                //批量入库
                if (CollUtil.isNotEmpty(elements)) {
                    threadPoolDataService.upsertBatchSavePoolData(elements);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 2, TimeUnit.SECONDS);
    }

    public static void pushData(ThreadPoolData threadPoolData) {
        if (IS_START.get()) {
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            redisTemplate.opsForValue().set(String.format(RedisCacheKey.INSTANCE_ACTIVE_CACHE, threadPoolData.getServerKey(), threadPoolData.getInstanceKey()), "活跃", 30, TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(String.format(RedisCacheKey.THREAD_ACTIVE_CACHE, threadPoolData.getServerKey(), threadPoolData.getInstanceKey(), threadPoolData.getThreadPoolName()), "活跃", 30, TimeUnit.SECONDS);

            //验证是否存在变化
            if(comparedWithLastTime(threadPoolData)) {
                return;
            }
            String serverKey = threadPoolData.getServerKey();
            String instanceKey = threadPoolData.getInstanceKey();
            InstanceItemService instanceItemService = SpringUtil.getBean(InstanceItemService.class);
            threadPoolData.setInstanceId(instanceItemService.findByInstanceNameAndServerNameOrCreateOnCache(serverKey, instanceKey));
            if (!THREAD_POOL_DATA.offer(threadPoolData)) {
                log.warn("Existential task dropping {}, Please set the parameter -Dthread.pool.data.queue.size=value(value greater than {})", JSONUtil.toJsonStr(threadPoolData), QUEUE_SIZE);
            }
        } else {
            log.error("com.threadx.metrics.server.init.ThreadPoolDataConsumer spring bean Be destroyed.");
        }
    }


    /**
     * 对当前的线程池数据生成一个md5,与redis内的数据md5做对比，如果相同则返回true,否则返回false
     *
     * @param threadPoolData 线程池数据
     * @return 与上一次的数据是否相同
     */
    public static boolean comparedWithLastTime(ThreadPoolData threadPoolData) {
        if (threadPoolData != null) {
            //redis取值
            StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
            String serverKey = threadPoolData.getServerKey();
            String instanceKey = threadPoolData.getInstanceKey();
            String address = threadPoolData.getAddress();
            String threadPoolName = threadPoolData.getThreadPoolName();
            //数据缓存主键
            String cacheKey = String.format(RedisCacheKey.THREAD_POOL_LAST_DATA_CACHE, serverKey, instanceKey, address, threadPoolName);

            ThreadPoolData threadPoolDataNew = new ThreadPoolData();
            BeanUtil.copyProperties(threadPoolData, threadPoolDataNew);
            //置空变化对象
            threadPoolDataNew.setAddress(null);
            threadPoolDataNew.setCreateTime(null);
            threadPoolDataNew.setUpdateTime(null);
            threadPoolDataNew.setId(null);
            threadPoolDataNew.setInstanceId(null);
            // 获取当前数据的md5
            String threadPoolDataJson = JSONUtil.toJsonStr(threadPoolDataNew);
            String threadPoolDataMd5 = SecureUtil.md5(threadPoolDataJson);

            //获取上一次的数据
            String preThreadDataMd5 = redisTemplate.opsForValue().get(cacheKey);
            //将本次的数据放到redis中更新
            redisTemplate.opsForValue().set(cacheKey, threadPoolDataMd5, 1, TimeUnit.DAYS);


            if(StrUtil.isNotBlank(preThreadDataMd5)) {
                return threadPoolDataMd5.equals(preThreadDataMd5);
            }
            return false;
        }
        return true;
    }
}
