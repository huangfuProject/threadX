package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * *************************************************<br/>
 * <br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/5 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程池数据的映射对象", value = "线程池数据的映射对象")
public class InstanceItemDataVo {



    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 监控时长
     */
    @ApiModelProperty(name = "monitoringDuration", value = "监控时长")
    private String monitoringDuration;

    /**
     * 当前实例内的线程池的数量
     */
    @ApiModelProperty(name = "threadPoolCount", value = "当前实例内的线程池的数量")
    private Integer threadPoolCount;

    /**
     * 活跃的线程池的数量
     */
    @ApiModelProperty(name = "activeThreadPoolCount", value = "活跃的线程池的数量")
    private Integer activeThreadPoolCount;

    /**
     * 等待的线程池的数量
     */
    @ApiModelProperty(name = "waitThreadPoolCount", value = "等待的线程池的数量")
    private Integer waitThreadPoolCount;

}
