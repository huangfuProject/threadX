package com.threadx.check.enums;

import com.threadx.check.ThreadPoolExecutorInterceptCheck;
import com.threadx.check.impl.IncludeTokenThreadPoolExecutorInterceptCheck;
import com.threadx.log.Logger;
import com.threadx.log.factory.ThreadXAgetySystemLoggerFactory;
import com.threadx.utils.ReflectionUtils;

/**
 * 拦截检查的插件枚举
 *
 * @author huangfukexing
 * @date 2023/3/14 14:31
 */
public enum InterceptCheckEnum {

    /**
     * 代理新建的检查
     */
    PROXY_CREATE_CHECK("ProxyCreateThreadPoolExecutorInterceptCheck", IncludeTokenThreadPoolExecutorInterceptCheck.class);
    ;
    private final static Logger logger = ThreadXAgetySystemLoggerFactory.getLogger(InterceptCheckEnum.class);
    private final String checkName;
    private final Class<? extends ThreadPoolExecutorInterceptCheck> interceptCheckClass;

    InterceptCheckEnum(String checkName, Class<? extends ThreadPoolExecutorInterceptCheck> interceptCheckClass) {
        this.checkName = checkName;
        this.interceptCheckClass = interceptCheckClass;
    }

    /**
     * 执行所有检查
     * @return 是否所有的检查全部通过
     */
    public static boolean allCheck() throws Exception {
        InterceptCheckEnum[] interceptCheckEnums = InterceptCheckEnum.values();
        for (InterceptCheckEnum interceptCheckEnum : interceptCheckEnums) {
            String checkName = interceptCheckEnum.checkName;
            Class<? extends ThreadPoolExecutorInterceptCheck> interceptCheckClass = interceptCheckEnum.interceptCheckClass;
            ThreadPoolExecutorInterceptCheck interceptCheck = ReflectionUtils.newInstanceConstructor(interceptCheckClass);
            logger.debug("check name: {}" , checkName);
            if (!interceptCheck.interceptCheck()) {
                logger.debug("check name {} not Pass the verification Result" , checkName);
                return false;
            }
            logger.debug("check name {} Pass the verification Result" , checkName);
        }
        logger.debug("All the tests passed");
        return true;
    }
}
