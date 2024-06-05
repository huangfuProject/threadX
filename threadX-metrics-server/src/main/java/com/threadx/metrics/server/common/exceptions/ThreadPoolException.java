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
public class ThreadPoolException extends IException {
    private static final long serialVersionUID = -7485773307228894576L;

    public ThreadPoolException(String message) {
        super(message);
    }

    public ThreadPoolException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public ThreadPoolException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public ThreadPoolException(Throwable cause) {
        super(cause);
    }
}
