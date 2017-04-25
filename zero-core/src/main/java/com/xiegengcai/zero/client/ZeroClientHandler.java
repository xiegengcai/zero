package com.xiegengcai.zero.client;

import com.xiegengcai.zero.Dispatcher;
import com.xiegengcai.zero.common.Constants;
import com.xiegengcai.zero.packet.ResponsePacket;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
//@ChannelHandler.Sharable
public class ZeroClientHandler extends SimpleChannelInboundHandler<ResponsePacket> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    // ChannelGroup用于保存所有连接的客户端，注意要用static来保证只有一个ChannelGroup实例，否则每new一个TcpServerHandler都会创建一个ChannelGroup
//    protected static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Dispatcher dispatcher;

    public ZeroClientHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    protected void channelRead0(ChannelHandlerContext ctx, ResponsePacket packet) throws Exception {
//        System.out.println(packet.getLength());
        System.out.println("服务端响应：序列号="+packet.getSequenceId() + "，指令号="+packet.getCmd() + ", 响应码="+packet.getCode());
//        System.out.println(packet.getCmd());
        if (packet.getCode() == Constants.ErrorCode.SUCCESS.getCode()) {
            dispatcher.dispatch(packet, ctx);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.err.println("断开连接");
//        ctx.pipeline().disconnect();
        ChannelFuture future = ctx.channel().close();
        while (true) {
            if (future.isSuccess()) {
                System.exit(0);
            }
        }

    }
}
