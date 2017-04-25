package com.xiegengcai.zero.server;

import com.google.common.base.Throwables;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.xiegengcai.zero.Dispatcher;
import com.xiegengcai.zero.ZeroFilterValue;
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
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
//@Component("dispatcher")
public class ZeroServiceDispatch implements Dispatcher<RequestPacket, ResponsePacket> {
    private final Logger logger = LoggerFactory.getLogger(ZeroServiceDispatch.class);

    @Autowired
    private ZeroContext zeroContext;
//    @Autowired
//    private ServerConf serverConf;

    @Override
    public ResponsePacket dispatch(RequestPacket request, ChannelHandlerContext ctx) throws ZeroException {
        // 获取处理器
        SpiMethodHandler spiMethodHandler = zeroContext.getSpiMethodHandler(request.getCmd(), request.getVersion());
        ResponsePacket response = new ResponsePacket();
        response.setSequenceId(request.getSequenceId());
        response.setCmd((short) (request.getCmd())); // 终端请求指令+1000返回
        if (spiMethodHandler == null) {
            logger.error("指令{}#{}不存在", request.getCmd(), request.getVersion());
            response.setCode(Constants.ErrorCode.SERVICE_ERROR.getCode());
            return response;
        }
        if (spiMethodHandler.getSpiMethodValue().isObsoleted()) {
            logger.error("指令{}#{}已过期", request.getCmd(), request.getVersion());
            response.setCode(Constants.ErrorCode.OBSOLETED_METHOD.getCode());
            return response;
        }
        return doInvoke(request, ctx, spiMethodHandler, response);
    }

    private ResponsePacket doInvoke(RequestPacket request, ChannelHandlerContext ctx, SpiMethodHandler spiMethodHandler, ResponsePacket response) {
        int errorCode = doBefore(request, ctx);
        if (errorCode != Constants.ErrorCode.SUCCESS.getCode()) {
            response.setCode(errorCode);
            logger.info("指令{}#{}前置拦截器返回错误：code={}", request.getCmd(), request.getVersion(), errorCode);
            return response;
        }
        try {
            Class<?> [] paramterTypes =spiMethodHandler.getHandlerMethod().getParameterTypes();
            List<Object> params = new ArrayList() ;
            if (paramterTypes.length > 0) {
                for (Class<?> paramterType : paramterTypes) {
                    if (ClassUtils.isAssignable(byte[].class, paramterType)) {
                        params.add(request.getBody());
                    } else if (ClassUtils.isAssignable(ChannelHandlerContext.class, paramterType)) {
                        params.add(ctx);
                    } else if (ClassUtils.isAssignable(int.class, paramterType)) {
                        params.add(request.getSequenceId());
                    }
                }
            }
            Object result = spiMethodHandler.getHandlerMethod().invoke(spiMethodHandler.getHandler(), params.toArray());
            MessageLite message = (MessageLite) result;
            errorCode = doAfter(request, message, ctx);
            if (errorCode != Constants.ErrorCode.SUCCESS.getCode()) {
                response.setCode(errorCode);
                logger.info("指令{}#{}后置置拦截器返回错误：code={}", request.getCmd(), request.getVersion(), errorCode);
                return response;
            }
            response.setCode(Constants.ErrorCode.SUCCESS.getCode());
            // Protobuf 序列化
            response.setBody(message.toByteArray());
        } catch (ZeroBizException e) {
            logger.error("指令{}#{}业务异常，code={}，message={}", request.getCmd(), request.getVersion(), e.getCode(), e.getMessage());
            response.setCode(e.getCode());
        }catch (Exception e) {
            if (e instanceof InvalidProtocolBufferException || e.getCause() instanceof InvalidProtocolBufferException) {
                logger.error("指令{}#{}数据包格式错误，{}", request.getCmd(), request.getVersion(), Throwables.getStackTraceAsString(e));
                response.setCode(Constants.ErrorCode.BODY_FORMAT_ERROR.getCode());
            }
            if (e.getCause() instanceof ZeroBizException) {
                ZeroBizException rpcException = (ZeroBizException) e.getCause();
                logger.error("指令{}#{}业务异常，code={}，message={}", request.getCmd(), request.getVersion(), rpcException.getCode(), rpcException.getMessage());
                response.setCode(rpcException.getCode());
            } else {
                logger.error("指令{}#{}未知错误, {}", request.getCmd(), request.getVersion(), Throwables.getStackTraceAsString(e));
                response.setCode(Constants.ErrorCode.UNKNOW_ERROR.getCode());
            }
        }
        return response;
    }

    private int doBefore(RequestPacket request, ChannelHandlerContext ctx) {
        for(ZeroFilterValue filterValue : zeroContext.getFilters()) {
            if (!filterValue.getFilter().isMatch(request)){
                continue;
            }
            int errorCode = filterValue.getFilter().before(request, ctx);
            if (errorCode != Constants.ErrorCode.SUCCESS.getCode()) {
                return errorCode;
            }
        }
        return Constants.ErrorCode.SUCCESS.getCode();
    }

    private int doAfter(RequestPacket request, MessageLite message, ChannelHandlerContext ctx) {
        for(ZeroFilterValue filterValue : zeroContext.getFilters().descendingSet()) {
            if (!filterValue.getFilter().isMatch(request)){
                continue;
            }
            int errorCode = filterValue.getFilter().after(message, ctx);
            if (errorCode != Constants.ErrorCode.SUCCESS.getCode()) {
                return errorCode;
            }
        }
        return Constants.ErrorCode.SUCCESS.getCode();
    }
}