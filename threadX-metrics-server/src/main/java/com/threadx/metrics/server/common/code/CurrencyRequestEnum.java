package com.threadx.metrics.server.common.code;

/**
 * 常规的、通用的状态码定义
 *
 * @author huangfu
 * @date 2022年11月1日18:17:13
 */
public enum CurrencyRequestEnum implements IExceptionCode {

    /**
     * 请求成功的状态码
     */
    REQUEST_SUCCESS("000000", "请求成功"),
    /**
     * 常规的处理失败的状态码
     */
    REQUEST_ERROR("500000", "处理失败"),
    /**
     * 参数缺失
     */
    PARAMETER_MISSING("500001", "参数缺失"),

    /**
     * 数据异常
     */
    DATA_EXCEPTION("500002", "数据异常"),
    ;
    /**
     * 状态码
     */
    private final String code;

    /**
     * 成功信息
     */
    private final String message;

    CurrencyRequestEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
