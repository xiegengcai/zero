package com.xiegengcai.zero.exception;

/**
 * 框架运行异常定义
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public class ZeroException extends RuntimeException {
    public ZeroException() {
        super();
    }

    public ZeroException(String message) {
        super(message);
    }

    public ZeroException(Throwable cause) {
        super(cause);
    }

    public ZeroException(String message, Throwable cause) {
        super(message, cause);
    }
}
