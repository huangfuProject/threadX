package com.threadx.utils;

import com.threadx.check.enums.InterceptCheckEnum;

/**
 * 校验确认是否通过的工具类
 *
 * @author huangfukexing
 * @date 2023/3/14 14:17
 */
public class ConfirmCheckUtil {

    /**
     * 是否需要拦截
     *
     * @return true需要拦截   false不需要拦截
     */
    public static boolean isIntercept() {
        try {
            return InterceptCheckEnum.allCheck();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
