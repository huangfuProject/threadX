package com.threadx.metrics.server.common.exceptions;

import com.threadx.metrics.server.common.code.IExceptionCode;

/**
 * *************************************************<br/>
 * 权限异常<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:22
 */
public class PermissionException extends IException {
    private static final long serialVersionUID = -7485773307228894576L;

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public PermissionException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }
}
