package com.threadx.thread;

import java.util.UUID;

/**
 * *************************************************<br/>
 * 业务线程使用的任务包装器<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/3/20 18:11
 */
public class BusinessThreadXRunnable implements Runnable {

    private final String taskId;
    private final Runnable runnable;

    public BusinessThreadXRunnable(Runnable runnable) {
        this.taskId = UUID.randomUUID().toString();
        this.runnable = runnable;
    }

    @Override
    public void run() {
        runnable.run();
    }

    public String getTaskId() {
        return taskId;
    }
}
