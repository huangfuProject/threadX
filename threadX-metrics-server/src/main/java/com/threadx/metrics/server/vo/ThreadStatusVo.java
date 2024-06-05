package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池状态
 *
 * @author huangfukexing
 * @date 2023/7/12 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "线程池数据的映射对象", value = "线程池数据的映射对象")
public class ThreadStatusVo implements Serializable {
    private static final long serialVersionUID = -7751700218081990395L;

    /**
     * 线程池的名称
     */
    @ApiModelProperty(name = "threadPoolName", value = "线程池的名称")
    private String threadPoolName;

    /**
     * 是否活跃   0 等待   1 活跃
     */
    @ApiModelProperty(name = "hasActive", value = "是否活跃   0 等待   1 活跃")
    private Integer hasActive;
}
