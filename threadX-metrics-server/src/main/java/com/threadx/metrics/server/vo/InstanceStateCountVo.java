package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实例状态数量的vo
 *
 * @author huangfukexing
 * @date 2023/7/12 14:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "实例状态数量的vo", value = "实例状态数量的vo")
public class InstanceStateCountVo implements Serializable {
    private static final long serialVersionUID = 5659529684459225577L;

    /**
     * 总数量
     */
    @ApiModelProperty(name = "totalCount", value = "总数量")
    private Integer totalCount;

    /**
     * 等待数量
     */
    @ApiModelProperty(name = "waitCount", value = "等待数量")
    private Integer waitCount;

    /**
     * 活跃数量
     */
    @ApiModelProperty(name = "activeCount", value = "活跃数量")
    private Integer activeCount;
}
