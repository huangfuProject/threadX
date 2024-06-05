package com.threadx.metrics.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户映射信息
 *
 * @author huangfukexing
 * @date 2023/6/1 07:53
 */
@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 7045262567616349898L;

    /**
     * 用户的id
     */
    private Long id;

    /**
     * 角色id
     */
    private List<Long> roleIds;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户的登录名
     */
    private String userName;

    /**
     * 用户的邮箱
     */
    private String email;

    /**
     * 用户的创建时间
     */
    private Long createTime;

    /**
     * 用户的修改时间
     */
    private Long updateTime;
}
