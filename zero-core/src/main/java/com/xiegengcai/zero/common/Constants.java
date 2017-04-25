package com.xiegengcai.zero.common;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.
 */
public interface Constants {

    public enum ErrorCode {
        UNKNOW_ERROR(210000, "未知错误")
        , SUCCESS(0,"SUCCESS")
        , SERVICE_ERROR(210001, "方法或版本错误")
        , OBSOLETED_METHOD(210002, "方法已过期")
        , BODY_FORMAT_ERROR(210003, "包体格式错误")
        ;

        private int code;
        private String message;

        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
