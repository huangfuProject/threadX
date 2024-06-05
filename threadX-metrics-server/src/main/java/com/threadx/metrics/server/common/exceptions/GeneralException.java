package com.threadx.metrics.server.common.exceptions;

import com.threadx.metrics.server.common.code.IExceptionCode;

/**
 * *************************************************<br/>
 * 常规性的通用异常<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 10:48
 */
public class GeneralException extends IException{
    private static final long serialVersionUID = 8789055121640423678L;

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }

    public GeneralException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode, cause);
    }

    public GeneralException(Throwable cause) {
        super(cause);
    }
}
