package com.threadx.metrics.server.common.exceptions;


import com.threadx.metrics.server.common.code.DefaultExceptionCode;
import com.threadx.metrics.server.common.code.IExceptionCode;
import com.threadx.utils.ThreadXThrowableMessageUtil;

/**
 * 异常基类
 *
 * @author huangfu
 * @date 2022年11月1日17:52:35
 */
public abstract class IException extends RuntimeException {


    private static final long serialVersionUID = -2152311181435749069L;

    /**
     * 错误码
     */
    private final IExceptionCode iExceptionCode;

    public IException(String message) {
        super(message);
        this.iExceptionCode = new DefaultExceptionCode(message);
    }

    public IException(IExceptionCode iExceptionCode) {
        super(iExceptionCode.defaultMessage());
        this.iExceptionCode = iExceptionCode;
    }

    public IException(IExceptionCode iExceptionCode, Throwable cause) {
        super(iExceptionCode.defaultMessage(), cause);
        this.iExceptionCode = iExceptionCode;
    }

    public IException(Throwable cause) {
        super(cause);
        this.iExceptionCode = new DefaultExceptionCode(ThreadXThrowableMessageUtil.messageRead(cause, true));
    }

    /**
     * 错误信息的基类信息
     *
     * @return 返回错误信息
     */
    public IExceptionCode getExceptionCode() {
        return this.iExceptionCode;
    }
}
