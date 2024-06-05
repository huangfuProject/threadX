package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * 用户角色查询
 *
 * @author huangfukexing
 * @date 2023/7/24 10:27
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
