package com.threadx.metrics.server.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.threadx.metrics.server.entity.ActiveLog;
import com.threadx.metrics.server.service.ActiveLogService;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * *************************************************<br/>
 * 日志信息消费<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:36
 */
@Slf4j
@Component("logMessageConsumer")
public class LogMessageConsumer implements InitializingBean {
    private final static String QUEUE_SIZE = System.getProperties().getProperty("threadx.async.log.queue.size", "4096");
    private final static MpscArrayQueue<ActiveLog> LOG_MESSAGE_DATA = new MpscArrayQueue<>(Integer.parseInt(QUEUE_SIZE));

    private final ActiveLogService activeLogService;

    private final static ScheduledThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(1, r -> {
        Thread thread = new Thread(r);
        thread.setName("logMessageConsumer");
        return thread;
    });

    public LogMessageConsumer(ActiveLogService activeLogService) {
        this.activeLogService = activeLogService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        THREAD_POOL.scheduleAtFixedRate(() -> {
            try {
                List<ActiveLog> elements = new ArrayList<>();
                MessagePassingQueue.Consumer<ActiveLog> consumer = elements::add;
                LOG_MESSAGE_DATA.drain(consumer, 50);
                //批量入库
                if (CollUtil.isNotEmpty(elements)) {
                    activeLogService.batchSave(elements);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 3, TimeUnit.SECONDS);
    }

    /**
     * 追加日志
     *
     * @param activeLog 日志信息
     */
    public static void addLog(ActiveLog activeLog) {
        if (!LOG_MESSAGE_DATA.offer(activeLog)) {
            log.warn("The log asynchronous queue is full, and the log data is discarded as follows: {}. , Please set the parameter -Dthreadx.async.log.queue.size=value(value greater than {}", JSONUtil.toJsonStr(activeLog), QUEUE_SIZE);
        }
    }
}
