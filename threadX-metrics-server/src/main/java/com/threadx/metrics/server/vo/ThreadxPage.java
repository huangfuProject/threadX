package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象
 *
 * @author huangfukexing
 * @date 2023/5/10 16:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "分页查询结果集", value = "分页查询结果集")
public class ThreadxPage<T> implements Serializable {
    private static final long serialVersionUID = -986464978979250370L;
    /**
     * 分页数据
     */
    @ApiModelProperty(name = "data", value = "分页数据")
    private List<T> data;
    /**
     * 数据总量
     */
    @ApiModelProperty(name = "total", value = "数据总量")
    protected long total = 0;
}
