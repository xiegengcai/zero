package com.xiegengcai.zero;

import com.xiegengcai.zero.exception.ZeroException;
import com.xiegengcai.zero.packet.Packet;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
public interface Dispatcher<R extends Packet, P extends Packet> {

    /**
     * 分发请求
     * @param request 请求包
     * @param ctx Context。目的是为了下层业务代码取到
     * @return
     * @throws ZeroException
     */
    P dispatch(R request, ChannelHandlerContext ctx) throws ZeroException;
}
