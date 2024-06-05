package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.RolePermission;
import org.springframework.stereotype.Repository;

/**
 * 角色 权限操作类
 *
 * @author huangfukexing
 * @date 2023/7/24 10:38
 */
@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermission> {
}
