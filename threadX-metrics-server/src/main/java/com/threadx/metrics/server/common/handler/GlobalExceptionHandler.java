package com.threadx.metrics.server.common.handler;

import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.code.IExceptionCode;
import com.threadx.metrics.server.common.exceptions.IException;
import com.threadx.metrics.server.common.result.ResponseResult;
import com.threadx.utils.ThreadXThrowableMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author huangfu
 * @date 2022年11月1日18:25:24
 */
@Slf4j
@SuppressWarnings("all")
@RestControllerAdvice
public class GlobalExceptionHandler {

    public final static String CONVENTION_LOG = "网络异常，请稍后再试!";

    /**
     * 针对自定义异常的拦截实现
     *
     * @param e 异常信息
     * @return 包装好后的数据
     */
    @ExceptionHandler(IException.class)
    public ResponseResult iExceptionHandler(IException e) {
        IExceptionCode exceptionCode = e.getExceptionCode();
        log.error(exceptionCode.defaultMessage());
        log.error(ThreadXThrowableMessageUtil.messageRead(e));
        return ResponseResult.error(exceptionCode.getCode(), exceptionCode.getMessage());
    }

    /**
     * 针对全局异常的拦截实现
     *
     * @param e 异常信息
     * @return 包装好后的数据
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        log.error(ThreadXThrowableMessageUtil.messageRead(e, true));
        return ResponseResult.error(CurrencyRequestEnum.REQUEST_ERROR.getCode(), CONVENTION_LOG);
    }
}
