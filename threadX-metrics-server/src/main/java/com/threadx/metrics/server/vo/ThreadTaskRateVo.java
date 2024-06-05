package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 线程池值比例
 *
 * @author huangfukexing
 * @date 2023/7/6 17:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "成功率和拒绝比的数据载体", value = "成功率和拒绝比的数据载体")
public class ThreadTaskRateVo implements Serializable {
    private static final long serialVersionUID = -3648653814129533891L;

    /**
     * 拒绝比
     */
    @ApiModelProperty(name = "refuseRate", value = "拒绝比")
    private String refuseRate;

    /**
     * 成功率
     */
    @ApiModelProperty(name = "successRate", value = "成功率")
    private String successRate;
}
