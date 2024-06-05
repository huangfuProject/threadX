package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.Permission;
import org.springframework.stereotype.Repository;

/**
 * 权限数据库操作
 *
 * @author huangfukexing
 * @date 2023/6/1 14:42
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
}
