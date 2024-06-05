package com.threadx.metrics.server.controller;

import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.conditions.ThreadTaskConditions;
import com.threadx.metrics.server.entity.ThreadTaskData;
import com.threadx.metrics.server.service.ThreadTaskDataService;
import com.threadx.metrics.server.vo.ThreadTaskDataErrorTop;
import com.threadx.metrics.server.vo.ThreadTaskVo;
import com.threadx.metrics.server.vo.ThreadxPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * *************************************************<br/>
 * 线程池任务控制器<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/11 0:01
 */
@RestController
@GlobalResultPackage
@Api(tags = "线程池任务控制器")
@RequestMapping("/threadTaskData")
public class ThreadTaskDataController {

    private final ThreadTaskDataService threadTaskDataService;

    public ThreadTaskDataController(ThreadTaskDataService threadTaskDataService) {
        this.threadTaskDataService = threadTaskDataService;
    }

    @Login
    @ApiOperation(value = "线程池任务错误数据top10")
    @GetMapping("findThreadTaskDataErrorCalculationTop10")
    public List<ThreadTaskDataErrorTop> findThreadTaskDataErrorCalculationTop10(){
        return threadTaskDataService.findThreadTaskDataErrorCalculation(10);
    }

    @Login
    @ApiOperation(value = "分页查询线程任务数据")
    @PostMapping("findByThreadTaskConditions")
    public ThreadxPage<ThreadTaskVo> findByThreadTaskConditions(@RequestBody ThreadTaskConditions threadTaskConditions){
        return threadTaskDataService.findByThreadTaskConditions(threadTaskConditions);
    }
}
