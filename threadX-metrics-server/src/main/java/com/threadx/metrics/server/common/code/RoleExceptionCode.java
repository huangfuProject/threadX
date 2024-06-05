package com.threadx.metrics.server.common.code;

/**
 * 用户登录异常信息
 *
 * @author huangfukexing
 * @date 2023/6/1 09:22
 */
public enum RoleExceptionCode implements IExceptionCode{
    /**
     * 角色名称重复异常
     */
    NAME_DUPLICATION("550000", "角色名称不能重复！"),


    /**
     * 角色不存在
     */
    ROLE_IN_EXISTENCE("550001", "角色不存在！"),
    ;
    /**
     * 错误吗
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    RoleExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    ;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
