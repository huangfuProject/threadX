package com.threadx.metrics.server.common.exceptions;

import com.threadx.metrics.server.common.code.IExceptionCode;

/**
 * token验证异常
 *
 * @author huangfukexing
 * @date 2023/6/1 09:02
 */
public class TokenCheckException extends IException{
    private static final long serialVersionUID = -5698335647904211732L;

    public TokenCheckException(String message) {
        super(message);
    }

    public TokenCheckException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public TokenCheckException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public TokenCheckException(Throwable cause) {
        super(cause);
    }
}
