package com.threadx.metrics.server.common.annotations;

import java.lang.annotation.*;

/**
 * 需要登录
 *
 * @author huangfukexing
 * @date 2023/6/1 07:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Login {
}
