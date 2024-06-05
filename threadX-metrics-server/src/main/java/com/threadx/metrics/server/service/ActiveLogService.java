package com.threadx.metrics.server.service;


import com.threadx.metrics.server.conditions.LogFindConditions;
import com.threadx.metrics.server.entity.ActiveLog;
import com.threadx.metrics.server.vo.ThreadxPage;

import java.util.List;

/**
 * *************************************************<br/>
 * 操作日志服务<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:06
 */
public interface ActiveLogService {

    /**
     * 查询日志根据活动的key
     * @param logFindConditions 查询条件
     * @return 对应的日志信息
     */
    ThreadxPage<ActiveLog> findActiveLogByLogFindConditions(LogFindConditions logFindConditions);

    /**
     * 保存日志
     *
     * @param activeLog 活动的日志信息
     */
    void saveLog(ActiveLog activeLog);

    /**
     * 批量保存
     *
     * @param activeLogs 批量日志
     */
    void batchSave(List<ActiveLog> activeLogs);

    /**
     * 根据用户的id删除操作id
     *
     * @param userId 用户的id
     */
    void deleteLogByUserId(Long userId);
}
