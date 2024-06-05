package com.threadx.metrics.server.service;

import com.threadx.metrics.server.dto.UserInfoDto;
import com.threadx.metrics.server.dto.UserLoginDto;
import com.threadx.metrics.server.entity.User;
import com.threadx.metrics.server.vo.LoginUserVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户服务
 *
 * @author huangfukexing
 * @date 2023/6/1 07:53
 */
public interface UserService {

    /**
     * 查询用户  根据用户的id的集合
     *
     * @param userIds 用户的id
     * @return 用户信息
     */
    List<User> findUserByIds(Collection<Long> userIds);

    /**
     * 用户登录
     *
     * @param userLoginDto 用户登录的参数
     * @return 令牌
     */
    LoginUserVo login(UserLoginDto userLoginDto);

    /**
     * 根据用户名查询用户信息
     *
     * @param userName 用户名
     * @return 用户详情
     */
    User findByUserName(String userName);

    /**
     * 当前用户登出
     */
    void logout();
}
