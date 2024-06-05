package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.RoleMenu;
import org.springframework.stereotype.Repository;

/**
 * 角色 菜单关联操作类
 *
 * @author huangfukexing
 * @date 2023/7/24 10:37
 */
@Repository
public interface RoleMenuMapper  extends BaseMapper<RoleMenu> {
}
