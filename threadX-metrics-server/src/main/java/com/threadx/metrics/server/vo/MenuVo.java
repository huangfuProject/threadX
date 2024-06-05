package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单映射
 *
 * @author huangfukexing
 * @date 2023/7/24 16:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "菜单映射", value = "菜单映射")
public class MenuVo {

    @ApiModelProperty(name = "id", value = "菜单id")
    private Long id;

    @ApiModelProperty(name = "name", value = "菜单名称")
    private String name;

    @ApiModelProperty(name = "menuDesc", value = "菜单介绍")
    private String menuDesc;
}
