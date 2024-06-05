package com.threadx.state;

import com.threadx.cache.ThreadPoolIndexData;
import com.threadx.cache.ThreadPoolWeakReferenceCache;
import com.threadx.call.EventUpdateThreadPoolCall;
import com.threadx.description.context.AgentContext;
import com.threadx.listeners.ThreadPoolEventListener;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.thread.BusinessThreadXRejectedExecutionHandler;
import com.threadx.utils.ConfirmCheckUtil;
import com.threadx.utils.ThreadXStateEventManager;

import java.io.Serializable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 线程池状态
 *
 * @author huangfukexing
 * @date 2023/3/9 21:25
 */
public class ThreadPoolExecutorState implements Serializable {

    private static final long serialVersionUID = -8563010018920100713L;
    private static final Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(ThreadPoolExecutorState.class);

    private static final AtomicBoolean ADD_LISTENERS_STATUS = new AtomicBoolean(false);
    public static void init(ThreadPoolExecutor sourceThreadPoolExecutor) {
        if(ADD_LISTENERS_STATUS.compareAndSet(false, true)) {
            ThreadXStateEventManager.addListener(new ThreadPoolEventListener());
            //初始化修改器回调接口
            AgentContext.setUpdateThreadPoolCall(new EventUpdateThreadPoolCall());
        }
        if(ConfirmCheckUtil.isIntercept()) {
            RejectedExecutionHandler rejectedExecutionHandler = sourceThreadPoolExecutor.getRejectedExecutionHandler();
            logger.info("source RejectedExecutionHandler: {}", rejectedExecutionHandler);
            BusinessThreadXRejectedExecutionHandler newRejectedExecutionHandler = new BusinessThreadXRejectedExecutionHandler(rejectedExecutionHandler);
            sourceThreadPoolExecutor.setRejectedExecutionHandler(newRejectedExecutionHandler);
            logger.info("Package rejection strategy: {}", newRejectedExecutionHandler);
            ThreadPoolIndexData threadPoolIndexData = ThreadPoolWeakReferenceCache.setCache(sourceThreadPoolExecutor);
            logger.info("add thread Pool index data, thread pool name is {}， thread pool group name is {}", threadPoolIndexData.getThreadPoolName(), threadPoolIndexData.getThreadPoolGroupName());
        }


    }
}
