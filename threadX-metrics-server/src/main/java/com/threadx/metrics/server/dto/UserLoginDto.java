package com.threadx.metrics.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录参数
 *
 * @author huangfukexing
 * @date 2023/6/1 07:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户登录信息", value = "用户登录信息")
public class UserLoginDto implements Serializable {
    private static final long serialVersionUID = -3075513494779807979L;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * 用户密码
     */
    @ApiModelProperty(name = "password", value = "用户密码")
    private String password;
}
