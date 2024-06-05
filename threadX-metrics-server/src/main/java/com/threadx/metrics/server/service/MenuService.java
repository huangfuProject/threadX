package com.threadx.metrics.server.service;

import com.threadx.metrics.server.entity.Menu;
import com.threadx.metrics.server.vo.MenuVo;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务类
 *
 * @author huangfukexing
 * @date 2023/6/1 14:36
 */
public interface MenuService {

    /**
     * 查询菜单信息 根据用户的id查询符合条件的菜单
     *
     * @return 返回符合调价的菜单
     */
    List<Menu> findThisUserMenu();

    /**
     * 查询所有的菜单
     * @return 返回所有的菜单信息
     */
    List<MenuVo> findAllMenu();

    /**
     * 查询根据id的集合
     * @param ids id的集合
     * @return 所有菜单
     */
    List<Menu> findByIds(Collection<Long> ids);

}
