package com.threadx.metrics.server.common.annotations;

import com.threadx.metrics.server.enums.PermissionValue;

import java.lang.annotation.*;

/**
 * *************************************************<br/>
 * 用户权限<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserPermission {
    PermissionValue value();
}
