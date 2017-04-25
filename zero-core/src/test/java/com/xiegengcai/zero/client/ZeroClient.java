package com.xiegengcai.zero.client;

import com.google.common.base.Throwables;
import com.xiegengcai.demo.proto.Message;
import com.xiegengcai.zero.common.ChannelState;
import com.xiegengcai.zero.conf.ClientConf;
import com.xiegengcai.zero.exception.ZeroException;
import com.xiegengcai.zero.packet.RequestPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public class ZeroClient implements Client {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private volatile ChannelState state;
    private AtomicInteger sequenceId;
    private ClientConf clientConf;
    private ZeroClientInitializer clientInitializer;
    private NioEventLoopGroup bossGroup;
    private InetSocketAddress remoteAddress;
    private Channel channel;


    public ZeroClient(ClientConf clientConf, ZeroClientInitializer clientInitializer) {
        this.clientConf = clientConf;
        this.clientInitializer = clientInitializer;
        this.sequenceId = new AtomicInteger(1);
    }
    public void setClientInitializer(ZeroClientInitializer clientInitializer) {
        this.clientInitializer = clientInitializer;
    }

    @Override
    public InetSocketAddress remoteAddress() {
        if (state.isAliveState()) {
            return this.remoteAddress;
        }
        throw new RuntimeException("客户端未启动完成。");
    }

    @PostConstruct
    @Override
    public void open() throws InterruptedException {
        bossGroup = new NioEventLoopGroup(this.clientConf.bossThreadSize());
        remoteAddress = new InetSocketAddress(this.clientConf.host(), this.clientConf.port());
        state = ChannelState.INIT;
        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture future = bootstrap.group(this.bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_RCVBUF, 1024*1024)
                    .option(ChannelOption.SO_SNDBUF, 10*1024*1024)
                    // 设置维持链接的活跃，清除死链接
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // 设置关闭延迟发送
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(this.clientInitializer)
                    .connect(remoteAddress).sync();

            channel = future.channel();
            // Wait until the connection is closed.
            send((short)100, Message.User.newBuilder().setPassword("1112242").setName("Jacky").build().toByteArray());
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(Throwables.getStackTraceAsString(e));
        } finally {
            close();
        }
    }

    @PreDestroy
    @Override
    public void close() {
        if (state.isCloseState()) {
            logger.info("Server close fail: already close");
            return;
        }

        if (state.isUnInitState()) {
            logger.info("Server close Fail: don't need to close because node is unInit state");
            return;
        }
        // 设置close状态
        state = ChannelState.CLOSE;
        // close listen socket
        if (channel != null) {
            channel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void send(short cmd, byte[] body) {
        if(!channel.isActive() && this.state == ChannelState.CLOSE){

        }
        RequestPacket request = new RequestPacket();
        request.setVersion(1);
        request.setCmd(cmd);
        request.setSequenceId(sequenceId.getAndIncrement());
        request.setBody(body);
        if(!this.state.isInitState()) {
            throw new ZeroException("链接未初始化");
        }
        channel.writeAndFlush(request, channel.newPromise().addListener(future -> {
            logger.info("发送消息, cmd={}, version={}, seqId={}", cmd, request.getVersion(), request.getSequenceId());
        }));
    }
}
