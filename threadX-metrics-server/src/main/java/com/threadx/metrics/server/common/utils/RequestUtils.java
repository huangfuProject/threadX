package com.threadx.metrics.server.common.utils;


import cn.hutool.core.util.StrUtil;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * request工具类
 *
 * @author huangfukexing
 * @date 2023年6月3日 22点26分
 */
public class RequestUtils {
    public static final String UNKNOWN = "unknown";
    private static UASparser parser;

    static {
        try {
            parser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取浏览器信息
     *
     * @param request request
     * @return 浏览器信息
     */
    public static String getUserAgentBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        try {
            UserAgentInfo info = parser.parse(userAgent);
            return info.getUaFamily() + " " + info.getBrowserVersionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }

    /**
     * 获取操作系统的信息
     *
     * @param request request
     * @return 操作系统的信息
     */
    public static String getUserAgentOperatingSystem(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        try {
            UserAgentInfo info = parser.parse(userAgent);
            return info.getOsFamily() + " " + info.getOsName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "未知";
    }

    /**
     * 获取访问者的远程ip
     *
     * @param request 远程ip
     * @return ip地址
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if(StrUtil.isBlank(ipAddress) || UNKNOWN.equalsIgnoreCase(ipAddress)) {
            ipAddress = "未知";
        }
        return ipAddress;
    }
}
