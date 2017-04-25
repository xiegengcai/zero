package com.xiegengcai.zero;

import java.lang.reflect.Method;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
public class SpiMethodHandler {

    //处理器对象
    private Object handler;

    //处理器的处理方法
    private Method handlerMethod;

    // SpiMethod注解值
    private SpiMethodValue spiMethodValue;

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public SpiMethodValue getSpiMethodValue() {
        return spiMethodValue;
    }

    public void setSpiMethodValue(SpiMethodValue spiMethodValue) {
        this.spiMethodValue = spiMethodValue;
    }

//    public Class<? extends MessageLite> getRequestType() {
//        return requestType;
//    }
//
//    public void setRequestType(Class<? extends MessageLite> requestType) {
//        this.requestType = requestType;
//    }

}
