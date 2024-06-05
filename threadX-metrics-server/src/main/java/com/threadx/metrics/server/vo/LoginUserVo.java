package com.threadx.metrics.server.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录用户前端页面存储的基础信息
 *
 * @author huangfukexing
 * @date 2023/6/28 14:53
 */

@Data
@ApiModel(description = "登录后的用户简要信息", value = "登录后的用户简要信息")
public class LoginUserVo {
    private static final long serialVersionUID = -8879879402318962039L;
    /**
     * 用户的token信息
     */
    @ApiModelProperty(name = "token", value = "用户的登录票据")
    private String token;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickName", value = "用户昵称")
    private String nickName;
}
