package com.xiegengcai.zero.filter;

import com.google.protobuf.MessageLite;
import com.xiegengcai.zero.annotation.ZeroFilter;
import com.xiegengcai.zero.packet.RequestPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * <pre>
 *     过滤器接口
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/9/2.
 */
public abstract class Filter {

    /**
     * 是否拦截该指令
     * @param req
     * @return
     */
    public boolean isMatch(RequestPacket req) {
        ZeroFilter zeroFilter = this.getClass().getAnnotation(ZeroFilter.class);
        for (int cmd : zeroFilter.ignoreCmds()) {
            if (cmd == req.getCmd()) {
                return false;
            }
        }
        for (int cmd : zeroFilter.cmds()) {
            if (cmd == req.getCmd()) {
                return true;
            }
        }
        return true;
    }
    /**
     * 前置拦截
     *
     * @param req
     * @param ctx
     * @return
     */
    public abstract int before(RequestPacket req, ChannelHandlerContext ctx);

    /**
     * 后置拦截
     *
     * @param message
     * @param cxt
     * @return
     */
    public abstract int after(MessageLite message, ChannelHandlerContext cxt);
}
