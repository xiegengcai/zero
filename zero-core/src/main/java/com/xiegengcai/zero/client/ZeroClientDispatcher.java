package com.xiegengcai.zero.client;

import com.google.common.base.Throwables;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.xiegengcai.zero.Dispatcher;
import com.xiegengcai.zero.SpiMethodHandler;
import com.xiegengcai.zero.ZeroContext;
import com.xiegengcai.zero.common.Constants;
import com.xiegengcai.zero.exception.ZeroBizException;
import com.xiegengcai.zero.exception.ZeroException;
import com.xiegengcai.zero.packet.RequestPacket;
import com.xiegengcai.zero.packet.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-03-11.
 */
//@Component("clientDispatcher")
public class ZeroClientDispatcher implements Dispatcher<ResponsePacket, RequestPacket> {
    private final Logger logger = LoggerFactory.getLogger(ZeroClientDispatcher.class);
    @Autowired
    private ZeroContext zeroContext;

    private static final boolean HAS_PARSER;

    static {
        boolean hasParser = false;
        try {
            // MessageLite.getParsetForType() is not available until protobuf 2.5.0.
            MessageLite.class.getDeclaredMethod("getParserForType");
            hasParser = true;
        } catch (Throwable t) {
            // Ignore
        }

        HAS_PARSER = hasParser;
    }
    @Override
    public RequestPacket dispatch(ResponsePacket responsePacket, ChannelHandlerContext ctx) throws ZeroException {
        // 获取处理器
        SpiMethodHandler spiMethodHandler = zeroContext.getSpiMethodHandler(responsePacket.getCmd(), 1);

        Class<?> [] paramterTypes =spiMethodHandler.getHandlerMethod().getParameterTypes();
        List<Object> params = new ArrayList() ;
        try {
            if (paramterTypes.length > 0) {
                for (Class<?> paramterType : paramterTypes) {
                    if (ClassUtils.isAssignable(byte[].class, paramterType)) {
                        params.add(responsePacket.getBody());
                    } else if (ClassUtils.isAssignable(ChannelHandlerContext.class, paramterType)) {
                        params.add(ctx);
                    } else if (ClassUtils.isAssignable(int.class, paramterType)) {
                        params.add(responsePacket.getSequenceId());
                    }
                }
            }
            Object result = spiMethodHandler.getHandlerMethod().invoke(spiMethodHandler.getHandler(), params.toArray());
//            MessageLite message = (MessageLite) result;
        } catch (ZeroBizException e) {
            logger.error("指令{}业务异常，code={}，message={}", responsePacket.getCmd(), e.getCode(), e.getMessage());
        }catch (Exception e) {
            if (e instanceof InvalidProtocolBufferException || e.getCause() instanceof InvalidProtocolBufferException) {
                logger.error("指令{}数据包格式错误，{}", responsePacket.getCmd(), Throwables.getStackTraceAsString(e));
                responsePacket.setCode(Constants.ErrorCode.BODY_FORMAT_ERROR.getCode());
            }
            logger.error("指令{}未知错误, {}", responsePacket.getCmd(),  Throwables.getStackTraceAsString(e));
        }
        return null;
    }

}
