package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户映射
 *
 * @author huangfukexing
 * @date 2023/7/17 16:19
 */
@Data
@ApiModel(description = "用户角色相关的的基础信息", value = "用户角色相关的的基础信息")
public class UserRoleVo implements Serializable {

    private static final long serialVersionUID = -3573761440753162448L;
    /**
     * 用户的id
     */
    @ApiModelProperty(name = "id", value = "用户的id")
    private Long id;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickName", value = "用户昵称")
    private String nickName;

    /**
     * 用户的登录名
     */
    @ApiModelProperty(name = "userName", value = "用户的登录名")
    private String userName;

    /**
     * 用户密码
     */
    @ApiModelProperty(name = "password", value = "用户密码")
    private String password;

    /**
     * 用户的邮箱
     */
    @ApiModelProperty(name = "email", value = "用户的邮箱")
    private String email;

    /**
     * 当前存在的角色信息
     */
    @ApiModelProperty(name = "email", value = "用户的邮箱")
    private List<Long> selectRoleList;
}

