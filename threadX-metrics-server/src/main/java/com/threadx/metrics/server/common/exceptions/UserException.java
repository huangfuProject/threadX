package com.threadx.metrics.server.common.exceptions;

import com.threadx.metrics.server.common.code.IExceptionCode;

/**
 * @author huangfukexing
 * @date 2023/7/20 17:42
 */
public class UserException  extends IException {
    private static final long serialVersionUID = -2744683271935923344L;

    public UserException(IExceptionCode iExceptionCode) {
        super(iExceptionCode);
    }
}
