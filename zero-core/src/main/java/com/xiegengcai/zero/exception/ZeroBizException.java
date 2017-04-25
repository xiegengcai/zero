package com.xiegengcai.zero.exception;

/**
 * 框架业务异常定义
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public class ZeroBizException extends ZeroException{
    /**
     * 错误码
     */
    private int code;
    private String message;

    public ZeroBizException(int code) {
        this(code, null);
    }
    public ZeroBizException(int code, String message) {
        this(code, message, null);
    }

    public ZeroBizException(int code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
