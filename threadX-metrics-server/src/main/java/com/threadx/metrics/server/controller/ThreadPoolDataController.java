package com.threadx.metrics.server.controller;

import cn.hutool.json.JSONUtil;
import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Log;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.conditions.ThreadPoolDetailConditions;
import com.threadx.metrics.server.conditions.ThreadPoolPageDataConditions;
import com.threadx.metrics.server.dto.ThreadPoolVariableParameter;
import com.threadx.metrics.server.entity.ThreadPoolUpdateLog;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.ThreadPoolDataService;
import com.threadx.metrics.server.vo.ThreadPoolDataVo;
import com.threadx.metrics.server.vo.ThreadPoolDetailsVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * *************************************************<br/>
 * 线程池数据控制器<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/7/1 20:38
 */
@RestController
@GlobalResultPackage
@Api(tags = "线程池数据控制器")
@RequestMapping("/threadPool")
public class ThreadPoolDataController {

    private final ThreadPoolDataService threadPoolDataService;

    public ThreadPoolDataController(ThreadPoolDataService threadPoolDataService) {
        this.threadPoolDataService = threadPoolDataService;
    }

    @Login
    @ApiOperation(value = "查询线程池详情")
    @PostMapping("findThreadPoolDetail")
    public ThreadPoolDetailsVo findThreadPoolDetail(@RequestBody ThreadPoolDetailConditions threadPoolDetailConditions) {
        return threadPoolDataService.findThreadPoolDetail(threadPoolDetailConditions);
    }

    @Login
    @ApiOperation(value = "根据查询条件分页查询线程池")
    @PostMapping("findPageByThreadPoolPageDataConditions")
    public ThreadxPage<ThreadPoolDataVo> findPageByThreadPoolPageDataConditions(@RequestBody ThreadPoolPageDataConditions threadPoolPageDataConditions) {
        return threadPoolDataService.findPageByThreadPoolPageDataConditions(threadPoolPageDataConditions);
    }

    /**
     * 查询线程池的核心参数
     *
     * @param threadPoolDataId 线程池的id
     * @return 对应线程池的核心参数
     */
    @Login
    @ApiOperation(value = "查询线程池的核心参数")
    @GetMapping("findThreadPoolParam")
    public ThreadPoolVariableParameter findThreadPoolParam(@RequestParam("threadPoolDataId") Long threadPoolDataId) {
        return threadPoolDataService.findThreadPoolParam(threadPoolDataId);
    }

    /**
     * 修改线程池参数
     *
     * @param threadPoolVariableParameter 线程池参数
     */
    @Login
    @ApiOperation(value = "修改线程池的核心参数")
    @PostMapping("updateThreadPoolParam")
    @UserPermission(PermissionValue.UPDATE_THREAD_POOL_PARAM)
    @Log(value = LogEnum.UPDATE_THREAD_PARAM)
    public void updateThreadPoolParam(@RequestBody ThreadPoolVariableParameter threadPoolVariableParameter) {
        threadPoolDataService.updateThreadPoolParam(threadPoolVariableParameter);
    }

    /**
     * 查找线程池的修改日志
     *
     * @return 线程池的修改日志
     */
    @Login
    @ApiOperation(value = "查找线程池的修改日志")
    @GetMapping("findThreadPoolUpdateLog")
    public List<ThreadPoolUpdateLog> findThreadPoolUpdateLog() {
        return threadPoolDataService.findThreadPoolUpdateLog(10);
    }
}
