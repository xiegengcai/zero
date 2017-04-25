package com.xiegengcai.zero.client;

import com.xiegengcai.zero.client.codec.ZeroClientDecoder;
import com.xiegengcai.zero.client.codec.ZeroClientEncoder;
import com.xiegengcai.zero.codec.Codec;
import com.xiegengcai.zero.conf.ClientConf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ZeroClientInitializer extends ChannelInitializer<SocketChannel> {
    private Codec codec;
    private ZeroClientHandler handler;
    @Autowired
    private ClientConf conf;

    public ZeroClientInitializer(Codec codec, ZeroClientHandler handler) {
        this.codec = codec;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast("logger", new LoggingHandler(LogLevel.INFO))
                .addFirst(new LengthFieldBasedFrameDecoder(this.conf.maxFrameLength(), 0, this.conf.packetHeaderLength()
                        , -this.conf.packetHeaderLength(), 0))
                .addLast("decoder", new ZeroClientDecoder(this.codec))
                .addLast("encoder", new ZeroClientEncoder(this.codec))
                .addLast("idel", new IdleStateHandler(conf.readerIdle(), conf.writerIdle(), conf.bothIdle()))
                .addLast("handler", this.handler)
        ;
    }
}
