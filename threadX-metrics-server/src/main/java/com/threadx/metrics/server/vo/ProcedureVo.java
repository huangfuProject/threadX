package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 步骤Vo
 *
 * @author huangfukexing
 * @date 2023/7/13 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "步骤Vo", value = "步骤Vo")
public class ProcedureVo implements Serializable {
    private static final long serialVersionUID = -3779252779406509319L;

    /**
     * 步骤标题
     */
    @ApiModelProperty(name = "title", value = "步骤标题")
    private String title;

    /**
     * 步骤详细
     */
    @ApiModelProperty(name = "details", value = "步骤详细")
    private String details;
}
