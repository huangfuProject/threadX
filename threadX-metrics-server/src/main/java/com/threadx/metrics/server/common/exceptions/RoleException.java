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
public class RoleException extends IException {

    private static final long serialVersionUID = -4771338114285082823L;

    public RoleException(String message) {
        super(message);
    }

    public RoleException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public RoleException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public RoleException(Throwable cause) {
        super(cause);
    }
}
