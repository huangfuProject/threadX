package com.threadx.metrics.server.interceptors;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.code.LoginExceptionCode;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.common.exceptions.LoginException;
import com.threadx.metrics.server.common.utils.RequestUtils;
import com.threadx.metrics.server.common.utils.ThreadxJwtUtil;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.dto.RequestData;
import com.threadx.metrics.server.dto.UserDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 *
 * @author huangfukexing
 * @date 2023/6/1 15:57
 */
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        setRequestData(request);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(Login.class)) {
                String tokenKey = request.getHeader("threadX-token");
                if (StrUtil.isBlank(tokenKey)) {
                    //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new LoginException(LoginExceptionCode.USER_NOT_LOGIN_ERROR);
                }
                //获取用户的id
                String idStr = tokenKey.substring(tokenKey.length() - 1);
                int userId = Integer.parseInt(idStr);
                //根据tokenKey获取redis的token 信息
                String cacheKey = String.format(RedisCacheKey.USER_TOKEN_CACHE, userId, tokenKey);
                String oldToken = redisTemplate.opsForValue().get(cacheKey);
                if (StrUtil.isBlank(oldToken)) {
                    //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new LoginException(LoginExceptionCode.USER_NOT_LOGIN_ERROR);
                }
                // 进行令牌验证逻辑
                UserDto userDto = ThreadxJwtUtil.renewParseToken(oldToken, newToken -> redisTemplate.opsForValue().set(cacheKey, newToken, 120, TimeUnit.MINUTES));
                if (userDto == null) {
                    //response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new LoginException(LoginExceptionCode.USER_NOT_LOGIN_ERROR);
                } else {
                    LoginContext.setUserData(userDto);
                    return true;
                }
            }
        }
        return true;
    }

    private void setRequestData(HttpServletRequest request) {
        RequestData requestData = new RequestData();
        requestData.setBrowser(RequestUtils.getUserAgentBrowser(request));
        requestData.setIpAddress(RequestUtils.getRemoteIp(request));
        requestData.setOs(RequestUtils.getUserAgentOperatingSystem(request));
        LoginContext.setRequestData(requestData);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginContext.removeUserData();
        LoginContext.removeRequestData();
    }
}
