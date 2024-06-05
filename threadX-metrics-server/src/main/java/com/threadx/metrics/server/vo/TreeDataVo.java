package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 服务树的映射关系
 *
 * @author huangfukexing
 * @date 2023/7/11 14:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "树状图映射", value = "树状图映射结果集")
public class TreeDataVo implements Serializable {
    private static final long serialVersionUID = -8564329712147476788L;

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "数据id")
    private Long id;

    /**
     * 上级标签
     */
    @ApiModelProperty(name = "parentId", value = "上级标签")
    private Long parentId;

    /**
     * 标签
     */
    @ApiModelProperty(name = "label", value = "标签名称")
    private String label;

    /**
     * 子节点
     */
    @ApiModelProperty(name = "children", value = "子节点")
    private List<TreeDataVo> children;
}
