package com.threadx.metrics.server.common.handler;

import cn.hutool.json.JSONUtil;
import com.threadx.metrics.server.common.annotations.GlobalResultPackage;
import com.threadx.metrics.server.common.result.ResponseResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;

/**
 * 全局结果集封装拦截
 *
 * @author huangfu
 * @date 2022年11月1日18:33:25
 */

@RestControllerAdvice
@SuppressWarnings("all")
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Method method = methodParameter.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        //类上是否存在
        boolean classAnn = declaringClass.isAnnotationPresent(GlobalResultPackage.class);
        //类上不存在看方法上是否存在
        if (!classAnn) {
            //返回方法上是否存在
            return methodParameter.getAnnotatedElement().isAnnotationPresent(GlobalResultPackage.class);
        } else {
            return classAnn;
        }
    }

    /**
     * @param body               返回结果
     * @param methodParameter    方法参数
     * @param mediaType          元类型
     * @param aClass             后续
     * @param serverHttpRequest  后续
     * @param serverHttpResponse 后续
     * @return 最终结果集
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (!(body instanceof ResponseResult)) {
            ResponseResult<Object> success = ResponseResult.success(body);
            if (body instanceof String) {
                return JSONUtil.toJsonStr(success);
            }
            return success;
        }
        return body;
    }
}
