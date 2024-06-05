package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.User;
import org.springframework.stereotype.Repository;

/**
 * 用户数据库操作
 *
 * @author huangfukexing
 * @date 2023/6/1 08:14
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
