package com.threadx.metrics.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.threadx.metrics.server.entity.ServerItem;
import com.threadx.metrics.server.entity.ThreadPoolData;
import org.springframework.stereotype.Repository;

/**
 * *************************************************<br/>
 * 服务相关配置的mapper<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/5/8 16:17
 */
@Repository
public interface ServerItemMapper  extends BaseMapper<ServerItem> {
}
