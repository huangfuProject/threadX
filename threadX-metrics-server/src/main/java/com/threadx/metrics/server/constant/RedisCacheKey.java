package com.threadx.metrics.server.constant;

/**
 * *************************************************<br/>
 * redis缓存主键<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 17:00
 */
public interface RedisCacheKey {
    /**
     * threadX 缓存前缀
     */
    String THREADX_CADCHE = "threadx:cache:";

    /**
     * 用户级别缓存前缀  userId
     */
    String USER_CACHE = THREADX_CADCHE + "user:%s:";


    /**
     * 用户对于实例的点击计数 拼接用户的id
     */
    String USER_CLICK_INSTANCE_COUNT = THREADX_CADCHE + "instance:clickCount:%s";

    /**
     * 用户的token令牌缓存  占位符为    tokenId
     */
    String USER_TOKEN_CACHE = USER_CACHE + "token:%s";

    /**
     * 用户菜单缓存  占位符为  userId
     */
    String USER_MENU_CACHE = USER_CACHE + "authority:menu";

    /**
     * 所有的菜单信息
     */
    String MENU_ALL_CACHE = THREADX_CADCHE + "menu:all";

    /**
     * 所有的权限信息
     */
    String PERMISSION_ALL_CACHE = THREADX_CADCHE + "permission:all";

    /**
     * 用户权限缓存  占位符为  userId
     */
    String USER_PERMISSION_CACHE = USER_CACHE + "authority:permission";


    /**
     * 实例id缓存主键， 占位符为 服务名称   实例名称
     */
    String INSTANCE_ID_CACHE = THREADX_CADCHE + "instance:%s:%s";

    /**
     * 存活实例的id 服务名称:  实例名称
     */
    String INSTANCE_ACTIVE_CACHE = THREADX_CADCHE + "instance:active:%s:%s:status";

    /**
     * 存活线程池的id 服务名称:  实例名称  线程池名称
     */
    String THREAD_ACTIVE_CACHE = THREADX_CADCHE + "instance:active:%s:%s:%s:status";

    /**
     * 服务id缓存主键， 占位符为 服务名称
     */
    String SERVER_ID_CACHE = THREADX_CADCHE + "server:%s";

    /**
     * 线程池最后一次的数据缓存  占位符为  服务名称  实例名称  实例地址  线程池名称
     */
    String THREAD_POOL_LAST_DATA_CACHE = THREADX_CADCHE + "pool:last:%s:%s:%s:%s";
}
