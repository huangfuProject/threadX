package com.threadx.metrics.server.service;

import com.threadx.metrics.server.conditions.ServerItemFindConditions;
import com.threadx.metrics.server.entity.ServerItem;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.TreeDataVo;

import java.util.List;

/**
 * *************************************************<br/>
 * 服务配置的业务操作<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:14
 */
public interface ServerItemService {

    /**
     * 根据id查询实例数据
     *
     * @param id id信息
     * @return 服务信息
     */
    ServerItem selectById(Long id);

    /**
     * 查询所有的服务数据
     *
     * @param serverItemName 服务名称 为空则查询全部的数据
     * @return 所有的服务数据
     */
    List<ServerItem> findServerItem(String serverItemName);


    /**
     * 根据服务的id查询数据
     *
     * @param serverIds 要查询的服务的id
     * @return 当前的服务信息
     */
    List<ServerItem> findServerItemInId(List<Long> serverIds);

    /**
     * 查询服务信息 分页查询
     *
     * @param serverItemFindConditions 查询条件
     * @return 所有的数据
     */
    ThreadxPage<ServerItem> findServerItemByPage(ServerItemFindConditions serverItemFindConditions);

    /**
     * 查询服务 根据服务名
     *
     * @param serverName 服务名称
     * @return 服务配置
     */
    ServerItem findByName(String serverName);

    /**
     * 查询服务 根据服务名 不存在就新建一个
     *
     * @param serverName 服务名称
     * @return 查询到服务信息
     */
    ServerItem findByNameOrCreate(String serverName);

    /**
     * 查询服务和实例的列表数据
     * @return 树状图
     */
    List<TreeDataVo> findAllServerAndInstanceData();
}
