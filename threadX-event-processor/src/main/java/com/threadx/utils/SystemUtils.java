package com.threadx.utils;

/**
 * 系统相关工具类
 *
 * @author huangfukexing
 * @date 2023/3/17 15:36
 */
public class SystemUtils {
    private final static int SYSTEM_CORE_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 系统核心数量
     *
     * @return 核心系统数量
     */
    public static int getSystemCoreCount() {
        return SYSTEM_CORE_COUNT;
    }
}
