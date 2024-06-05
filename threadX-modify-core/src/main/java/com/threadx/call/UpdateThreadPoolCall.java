package com.threadx.call;

import com.threadx.metrics.ThreadPoolParam;

/**
 * 修改线程池的回调接口
 *
 * @author huangfukexing
 * @date 2023/8/18 14:49
 */
public interface UpdateThreadPoolCall {

    /**
     * 修改回调
     *
     * @param threadPoolParam 线程池参数
     * @throws Exception 修改的异常信息
     */
    void modify(ThreadPoolParam threadPoolParam) throws Exception;
}
