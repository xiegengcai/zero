package com.xiegengcai.zero.server;

import com.xiegengcai.zero.codec.Codec;
import com.xiegengcai.zero.conf.ServerConf;
import com.xiegengcai.zero.server.codec.ZeroServerDecoder;
import com.xiegengcai.zero.server.codec.ZeroServerEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ZeroServerInitializer extends ChannelInitializer<SocketChannel> {
    private Codec codec;
    private ZeroServerHandler handler;
    @Autowired
    private ServerConf serverConf;


    public ZeroServerInitializer(Codec codec, ZeroServerHandler handler) {
        this.handler = handler;
        this.codec = codec;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("logger", new LoggingHandler(LogLevel.INFO))
                .addFirst(new LengthFieldBasedFrameDecoder(serverConf.maxFrameLength(), 0, serverConf.packetHeaderLength()
                        , -serverConf.packetHeaderLength(), 0))
                .addLast("decoder", new ZeroServerDecoder(codec))
                .addLast("encoder", new ZeroServerEncoder(codec))
                .addLast("idel", new IdleStateHandler(serverConf.readerIdle(), serverConf.writerIdle(), serverConf.bothIdle()))
                .addLast("handler", handler)
        ;
    }

}
