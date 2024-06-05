package com.threadx.metrics.server.common.exceptions;

import com.threadx.metrics.server.common.code.IExceptionCode;

/**
 * 用户登录异常
 *
 * @author huangfukexing
 * @date 2023/6/1 09:21
 */
public class LoginException extends IException {
    private static final long serialVersionUID = 7433594311830183625L;

    public LoginException(String message) {
        super(message);
    }

    public LoginException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public LoginException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public LoginException(Throwable cause) {
        super(cause);
    }
}
