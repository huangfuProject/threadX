package com.threadx.metrics.server.common.context;

import com.threadx.metrics.server.dto.RequestData;
import com.threadx.metrics.server.dto.UserDto;

/**
 * 登录上下文
 *
 * @author huangfukexing
 * @date 2023/6/1 15:36
 */
public class LoginContext {

    private final static ThreadLocal<UserDto> USER_CONTEXT = new ThreadLocal<>();
    private final static ThreadLocal<RequestData> USER_REQUEST = new ThreadLocal<>();

    /**
     * 设置用户上下文
     *
     * @param userDto 用户信息
     */
    public static void setUserData(UserDto userDto) {
        USER_CONTEXT.set(userDto);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static UserDto getUserData() {
        return USER_CONTEXT.get();
    }

    /**
     * 删除本次用户信息
     */
    public static void removeUserData() {
        USER_CONTEXT.remove();
    }



    /**
     * 设置用户上下文
     *
     * @param requestData 请求信息
     */
    public static void setRequestData(RequestData requestData) {
        USER_REQUEST.set(requestData);
    }

    /**
     * 获取用户信息
     *
     * @return 请求信息
     */
    public static RequestData getRequestData() {
        RequestData requestData = USER_REQUEST.get();
        if(requestData == null) {
            requestData = new RequestData();
        }
        return requestData;
    }

    /**
     * 删除本次请求信息
     */
    public static void removeRequestData() {
        USER_REQUEST.remove();
    }
}
