package com.threadx.metrics.server.aspects;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.threadx.metrics.server.common.annotations.Log;
import com.threadx.metrics.server.common.context.LoginContext;
import com.threadx.metrics.server.constant.LogConstant;
import com.threadx.metrics.server.dto.RequestData;
import com.threadx.metrics.server.entity.ActiveLog;
import com.threadx.metrics.server.enums.LogEnum;
import com.threadx.metrics.server.init.LogMessageConsumer;
import com.threadx.metrics.server.dto.UserDto;
import com.threadx.utils.ThreadXThrowableMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * *************************************************<br/>
 * 日志切面<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 23:15
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(com.threadx.metrics.server.common.annotations.Log)")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        ActiveLog activeLog = new ActiveLog();
        activeLog.init();
        long startTime = System.currentTimeMillis();
        activeLog.setStartTime(startTime);
        Object result;

        // 获取方法上的@Log注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log logAnnotation = method.getDeclaredAnnotation(Log.class);
        try {
            String[] paramReplaces = logAnnotation.paramReplace();
            getParam(activeLog, joinPoint, paramReplaces);
            result = joinPoint.proceed();
            activeLog.setResultState(LogConstant.SUCCESS);
        } catch (Throwable throwable) {
            // 在这里可以获取到错误信息并进行处理
            String errorMessage = ThreadXThrowableMessageUtil.messageRead(throwable);
            activeLog.setErrorMessage(errorMessage);
            activeLog.setResultState(LogConstant.ERROR);
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            activeLog.setEndTime(endTime);
            //获取任务耗时
            long executionTime = endTime - startTime;
            activeLog.setOperationTime(executionTime);

            // 获取注解信息并进行处理
            if (logAnnotation != null) {
                LogEnum log = logAnnotation.value();
                activeLog.setActiveKey(log.getActiveKey());
                activeLog.setActiveLog(log.getLogMessage());
            }
            UserDto userData = LoginContext.getUserData();
            if (userData != null) {
                Long id = userData.getId();
                if (id != null) {
                    activeLog.setUserId(id);
                }
            }

            RequestData requestData = LoginContext.getRequestData();
            activeLog.setBrowser(requestData.getBrowser());
            activeLog.setOs(requestData.getOs());
            activeLog.setIpAddress(requestData.getIpAddress());

            LogMessageConsumer.addLog(activeLog);
        }

        return result;
    }

    private void getParam(ActiveLog activeLog, ProceedingJoinPoint joinPoint, String[] paramReplaces) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 遍历方法参数，获取参数信息并转换为字符串
        StringBuilder paramInfo = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Class<?> paramType = parameterTypes[i];
            // 将参数信息转换为字符串
            String argString = paramType.getSimpleName() + "=>" + paramToString(arg, paramType, paramReplaces);
            paramInfo.append(argString).append("\n");
        }
        activeLog.setParamData(paramInfo.toString());
    }

    /**
     * 参数转换为string
     *
     * @param arg       参数信息
     * @param paramType 参数类型
     * @return 字符串参数
     */
    private String paramToString(Object arg, Class<?> paramType, String[] paramReplaces) {
        if (Long.class.equals(paramType) || Integer.class.equals(paramType) || Double.class.equals(paramType) || Float.class.equals(paramType) || String.class.equals(paramType) || Character.class.equals(paramType)) {
            return String.valueOf(arg);
        } else {
            //将参数转换为map
            Map<String, Object> stringObjectMap = BeanUtil.beanToMap(arg);
            //尝试进行参数替换
            for (String paramReplace : paramReplaces) {
                String[] kv = paramReplace.split("=");
                if (kv.length == 2) {
                    stringObjectMap.put(kv[0], kv[1]);

                }
            }
            return JSONUtil.toJsonStr(stringObjectMap);
        }

    }

    /**
     * 参数格式化
     *
     * @param arg   对象
     * @param key   变换属性
     * @param value 变幻值
     */
    private void paramFormat(Object arg, String key, String value) {
        try {
            Field declaredField = arg.getClass().getDeclaredField(key);
            declaredField.setAccessible(true);
            Class<?> fieldType = declaredField.getType();
            if (fieldType.equals(Long.class)) {
                declaredField.set(arg, Long.parseLong(value));
            } else if (fieldType.equals(Integer.class)) {
                declaredField.set(arg, Integer.parseInt(value));
            } else if (fieldType.equals(Double.class)) {
                declaredField.set(arg, Double.parseDouble(value));
            } else if (fieldType.equals(Float.class)) {
                declaredField.set(arg, Float.parseFloat(value));
            } else if (fieldType.equals(Short.class)) {
                declaredField.set(arg, Short.parseShort(value));
            } else if (fieldType.equals(Byte.class)) {
                declaredField.set(arg, Byte.parseByte(value));
            } else if (fieldType.equals(Boolean.class)) {
                declaredField.set(arg, Boolean.parseBoolean(value));
            } else if (fieldType.equals(String.class)) {
                declaredField.set(arg, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
