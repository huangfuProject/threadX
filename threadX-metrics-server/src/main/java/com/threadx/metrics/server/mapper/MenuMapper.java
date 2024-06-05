package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.Menu;
import org.springframework.stereotype.Repository;

/**
 * 菜单数据库操作
 *
 * @author huangfukexing
 * @date 2023/6/1 14:35
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {
}
