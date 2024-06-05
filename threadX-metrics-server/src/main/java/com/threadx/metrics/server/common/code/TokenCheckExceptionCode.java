package com.threadx.metrics.server.common.code;

/**
 * token验证异常码
 *
 * @author huangfukexing
 * @date 2023/6/1 09:04
 */
public enum TokenCheckExceptionCode implements IExceptionCode {
    /**
     * 用户登录令牌异常
     */
    TOKEN_CHECK_ERROR("520000", "用户登录令牌异常！");
    ;
    /**
     * 错误吗
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    TokenCheckExceptionCode(String code, String message) {
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
