package com.threadx.metrics.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * *************************************************<br/>
 * 用户信息<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 10:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息", value = "用户信息")
public class UserInfoDto implements Serializable {
    private static final long serialVersionUID = -7999645033234241915L;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickName", value = "用户昵称")
    private String nickName;

    /**
     * 用户密码
     */
    @ApiModelProperty(name = "password", value = "用户密码")
    private String password;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(name = "email", value = "用户邮箱")
    private String email;
}
