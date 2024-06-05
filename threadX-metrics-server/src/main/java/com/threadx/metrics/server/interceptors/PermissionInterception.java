package com.threadx.metrics.server.interceptors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.threadx.metrics.server.common.annotations.Login;
import com.threadx.metrics.server.common.annotations.UserPermission;
import com.threadx.metrics.server.common.code.PermissionExceptionCode;
import com.threadx.metrics.server.common.exceptions.PermissionException;
import com.threadx.metrics.server.entity.Permission;
import com.threadx.metrics.server.enums.PermissionValue;
import com.threadx.metrics.server.service.PermissionService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * *************************************************<br/>
 * 权限拦截器<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:10
 */
public class PermissionInterception implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PermissionService permissionService = SpringUtil.getBean(PermissionService.class);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(Login.class) && method.isAnnotationPresent(UserPermission.class)) {
                UserPermission userPermission = method.getDeclaredAnnotation(UserPermission.class);
                PermissionValue userPermissionValue = userPermission.value();
                String userPermissionKey = userPermissionValue.getPermissionKey();
                List<Permission> thisUserPermission = permissionService.findThisUserPermission();
                List<Permission> permissions = thisUserPermission.stream().filter(e -> userPermissionKey.equals(e.getPermissionKey())).collect(Collectors.toList());
                if (CollUtil.isEmpty(permissions)) {
                    //response.setStatus(HttpServletResponse.SC_FORBIDDEN)
                    throw new PermissionException(PermissionExceptionCode.UNAUTHORIZED_OPERATION);
                }

            }
        }
        return true;
    }
}
