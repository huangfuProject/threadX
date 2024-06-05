package com.threadx.metrics.server.common.annotations;

import com.threadx.metrics.server.enums.LogEnum;

import java.lang.annotation.*;

/**
 * *************************************************<br/>
 * aop切面<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 22:58
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {
    /**
     * 活跃标记
     *
     * @return 标记信息
     */
    LogEnum value();

    /**
     * 目前只支持简单类型以及String
     *
     * @return 参数替换  {"fieldName=******"}
     */
    String[] paramReplace() default {};
}
