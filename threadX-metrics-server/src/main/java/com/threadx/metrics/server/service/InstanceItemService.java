package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.InstanceItemDataConditions;
import com.threadx.metrics.server.conditions.InstanceItemFindConditions;
import com.threadx.metrics.server.entity.InstanceItem;
import com.threadx.metrics.server.vo.InstanceItemDataVo;
import com.threadx.metrics.server.vo.InstanceItemVo;
import com.threadx.metrics.server.vo.ThreadxPage;

import java.util.Collection;
import java.util.List;

/**
 * *************************************************<br/>
 * 实例配置的业务操作<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:14
 */
public interface InstanceItemService {

    /**
     * 分页查询 根据条件进行分页查询
     *
     * @param conditions 查询条件
     * @return 分页后的数据
     */
    ThreadxPage<InstanceItemVo> findByPage(InstanceItemFindConditions conditions);

    /**
     * 根据ID查询数据
     *
     * @param ids 数据的集合
     * @return 查询到的数据
     */
    List<InstanceItem> findInIds(Collection<Long> ids);

    /**
     * 查询实例 根据服务的id
     *
     * @param ids 服务的id
     * @return 实例信息
     */
    List<InstanceItem> findInServerIds(Collection<Long> ids);


    /**
     * 查询最常使用的top10
     *
     * @return 当前用户常用的top10
     */
    List<InstanceItemVo> commonlyUsedTop10();


    /**
     * 查询 根据服务名和实例名称
     *
     * @param serverName   服务名称
     * @param instanceName 实例名称
     * @return 实例信息
     */
    InstanceItem findByInstanceNameAndServerName(String serverName, String instanceName);


    /**
     * 查询 根据服务名和实例名称 不存在就创建
     *
     * @param serverName   服务名称
     * @param instanceName 实例名称
     * @return 实例信息
     */
    InstanceItem findByInstanceNameAndServerNameOrCreate(String serverName, String instanceName);

    /**
     * 从缓存中查询 缓存没有查数据库  数据库没有就新增
     *
     * @param serverName   服务名称
     * @param instanceName 实例名称
     * @return 实例信息
     */
    Long findByInstanceNameAndServerNameOrCreateOnCache(String serverName, String instanceName);

    /**
     * 实例详情查询
     *
     * @param instanceId 实例的id
     * @return 实例的详情信息
     */
    InstanceItemDataVo instanceListeningState(Long instanceId);

    /**
     * 监测实例是否处于活跃状态   true活跃  false  断连
     *
     * @param serverName   服务名称
     * @param instanceName 实例名称
     * @return 实例活跃检查
     */
    boolean instanceActiveCheck(String serverName, String instanceName);

    /**
     * 根据id查询实例
     *
     * @param instanceId 实例的id
     * @return 实例信息
     */
    InstanceItem findById(Long instanceId);
}
