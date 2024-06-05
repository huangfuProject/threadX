package com.threadx.thread;

import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 监听程序的拒绝程序
 *
 * @author huangfukexing
 * @date 2023/3/17 16:13
 */
public class ListenerHandlerRejectedExecutionHandler implements RejectedExecutionHandler {
    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ListenerHandlerRejectedExecutionHandler.class);
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        logger.warn("Check the accumulation of thread pool collection event handlers to reach the maximum threshold");
        if (!executor.isShutdown()) {
            r.run();
        }else {
            logger.error("thread pool Shutdown, ListenerHandlerRejectedExecutionHandler Refuse to deal with !!!");
        }
    }
}
