package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 平均执行和平均等待的数据载体
 *
 * @author huangfukexing
 * @date 2023/7/6 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "平均执行和平均等待的数据载体", value = "平均执行和平均等待的数据载体")
public class ThreadTaskAvgVo implements Serializable {

    private static final long serialVersionUID = 968455349575194600L;
    /**
     * 执行平均耗时
     */
    @ApiModelProperty(name = "averageTimeConsuming", value = "执行平均耗时")
    private String averageTimeConsuming;

    /**
     * 平均等待耗时
     */
    @ApiModelProperty(name = "averageWaitTimeConsuming", value = "平均等待耗时")
    private String averageWaitTimeConsuming;
}
