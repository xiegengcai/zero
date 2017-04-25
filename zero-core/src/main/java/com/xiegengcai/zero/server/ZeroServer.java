package com.xiegengcai.zero.server;

import com.xiegengcai.zero.common.ChannelState;
import com.xiegengcai.zero.conf.ServerConf;
import com.xiegengcai.zero.exception.ZeroException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public class ZeroServer implements Server {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private volatile ChannelState state = ChannelState.UNINIT;

    @Autowired
    private ServerConf serverConf;
    private ZeroServerInitializer channelInitializer;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private InetSocketAddress localAddress;

    private Channel serverChannel;


    public void setChannelInitializer(ZeroServerInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    @Override
    public InetSocketAddress localAddress() {
        if (state.isAliveState()) {
            return this.localAddress;
        }
        throw new ZeroException("服务未启动完成。");
    }

    @PostConstruct
    @Override
    public void open() {
        bossGroup = new NioEventLoopGroup(serverConf.bossThreadSize());
        workerGroup = new NioEventLoopGroup(serverConf.wokerThreadSize());
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                // 设置链接缓冲池的大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_RCVBUF, 1024*1024)
                .option(ChannelOption.SO_SNDBUF, 10*1024*1024)
                // 设置维持链接的活跃，清除死链接
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 设置关闭延迟发送
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(this.channelInitializer)
        ;
        state = ChannelState.INIT;
        localAddress = new InetSocketAddress(serverConf.port());
        state = ChannelState.ALIVE;
        try {
            // 绑定端口，同步等待
            ChannelFuture f =b.bind(localAddress).sync();
            logger.info("Server started at port {}", localAddress.getPort());
            serverChannel = f.channel();
            // 等待监听端口关闭
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new ZeroException(e);
        } finally {
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
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
        if (serverChannel != null) {
            serverChannel.close();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public boolean isClosed() {
        return state.isCloseState();
    }

    @Override
    public boolean isAvailable() {
        return state.isAliveState();
    }
}
