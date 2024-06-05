package com.threadx.metrics.server.conditions;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户分页查询条件
 *
 * @author huangfukexing
 * @date 2023/7/18 15:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageConditions implements Serializable {


    private static final long serialVersionUID = 4819671128424753914L;
    /**
     * 服务名称
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * 服务名称
     */
    @ApiModelProperty(name = "nickName", value = "用户昵称")
    private String nickName;

    /**
     * 每一页显示的条数
     */
    @ApiModelProperty(name = "pageSize", value = "每一页显示的条数")
    private Integer pageSize = 20;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "pageNumber", value = "当前页码")
    private Integer pageNumber = 1;
}
