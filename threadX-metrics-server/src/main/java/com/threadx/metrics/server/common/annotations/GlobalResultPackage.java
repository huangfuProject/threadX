package com.threadx.metrics.server.common.annotations;

import java.lang.annotation.*;

/**
 * 全局结果封装
 *
 * @author huangfu
 * @date 2022年11月1日18:35:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface GlobalResultPackage {
}
