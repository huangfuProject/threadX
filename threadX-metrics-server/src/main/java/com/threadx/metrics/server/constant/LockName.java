package com.threadx.metrics.server.constant;

/**
 * *************************************************<br/>
 * 锁名称<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:45
 */
public interface LockName {

    String SERVER_CREATE_SELECT_LOCK = "threadx:lock:server_create_select";
    String INSTANCE_CREATE_SELECT_LOCK = "threadx:lock:instance_create_select";
}
