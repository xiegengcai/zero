package com.xiegengcai.zero.server;

import com.google.common.base.Throwables;
import com.xiegengcai.zero.Dispatcher;
import com.xiegengcai.zero.packet.Packet;
import com.xiegengcai.zero.packet.RequestPacket;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
@ChannelHandler.Sharable
public class ZeroServerHandler extends SimpleChannelInboundHandler<RequestPacket> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    // ChannelGroup用于保存所有连接的客户端，注意要用static来保证只有一个ChannelGroup实例，否则每new一个TcpServerHandler都会创建一个ChannelGroup
//    protected static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Dispatcher dispatcher;

    public ZeroServerHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 响应完成事件
     * @param ctx
     */
    protected void responseComplete(ChannelHandlerContext ctx, RequestPacket packet) {
        logger.info("Respone the request of seqId={}", packet.getSequenceId());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket packet) throws Exception {
//        ZeroServiceDispatch dispatcher =ZeroContext.getBean("dispatcher", ZeroServiceDispatch.class);
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("Receive Request from {} of {}, cmd={}, version={}, seqId={}"
                , remoteAddress.getAddress().getHostAddress(), ctx.channel().id().asLongText()
                , packet.getCmd(), packet.getVersion(), packet.getSequenceId()
        );
        Packet response = dispatcher.dispatch(packet, ctx);
        if (response != null) {
            ctx.writeAndFlush(response, new DefaultChannelPromise(ctx.channel()).addListener((ChannelFutureListener) channelFuture -> responseComplete(ctx, packet)));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 将新的连接加入到ChannelGroup，当连接断开ChannelGroup会自动移除对应的Channel
//        channels.add(ctx.channel());
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("channel[{}] from {}:{} actived.", ctx.channel().id().asLongText(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close(new DefaultChannelPromise(ctx.channel()).addListener((ChannelFutureListener) channelFuture -> {
            logger.error("channel[0x{}] exception, closed. {}", ctx.channel().id().asLongText(), Throwables.getStackTraceAsString(cause));
            ctx.close();
        }));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ctx.disconnect(ctx.newPromise().addListener((ChannelFutureListener) channelFuture -> {
            logger.error("channel[0x{}] handlerRemoved, closed. {}", ctx.channel().id().asLongText());
            ctx.close();
        }));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }
}

