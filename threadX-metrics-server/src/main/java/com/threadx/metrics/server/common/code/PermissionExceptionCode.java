package com.threadx.metrics.server.common.code;

/**
 * *************************************************<br/>
 * 权限异常的错误码<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:23
 */
public enum PermissionExceptionCode implements IExceptionCode {
    /**
     * 未授权操作，拒绝执行！
     */
    UNAUTHORIZED_OPERATION("530000", "未授权操作，拒绝执行！");
    /**
     * 错误吗
     */
    private final String code;

    /**
     * 错误信息
     */
    private final String message;

    PermissionExceptionCode(String code, String message) {
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
