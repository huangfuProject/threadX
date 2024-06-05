package com.threadx.metrics.server.common.code;

/**
 * 用户异常码
 *
 * @author huangfukexing
 * @date 2023/7/20 17:44
 */
public enum UserExceptionCode implements IExceptionCode {

    /**
     * 用户不存在
     */
    NOT_EXIST_USER("540000", "用户不存在！"),


    /**
     * 用户状态异常
     */
    USER_STATUS_EXCEPTION("540001", "用户状态异常！")
    ;
    /**
     * 错误吗
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    UserExceptionCode(String code, String message) {
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
