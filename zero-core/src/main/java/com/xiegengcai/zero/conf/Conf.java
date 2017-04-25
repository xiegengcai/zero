package com.xiegengcai.zero.conf;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public abstract class Conf {
    @Value("${server.port}")
    private int port;
    @Value("${boss.thread.size:1}")
    private int bossThreadSize;
    /**
     * 包头表示包长度占的长度
     */
    @Value("${packet.header.length:4}")
    private int packetHeaderLength;
    @Value("${packet.maxFrameLength:8192}")
    private int maxFrameLength;
    @Value("${channel.read.idle}")
    private int readerIdle;
    @Value("${channel.write.idle}")
    private int writerIdle;
    @Value("${channel.both.idle}")
    private int bothIdle;
    @Value("${max.timeout.interval:120}")
    private int maxTimeoutInterval;
    public int port() {
        return this.port;
    }
    public int bossThreadSize() {
        return this.bossThreadSize;
    }

    /**
     * 读空闲时间
     * @return
     */
    public int readerIdle() {
        return this.readerIdle;
    }

    /**
     * 写空闲时间
     * @return
     */
    public int writerIdle() {
        return this.writerIdle;
    }

    /**
     * 读写空闲时间
     * @return
     */
    public int bothIdle() {
        return this.bothIdle;
    }
    public int maxFrameLength() {
        return this.maxFrameLength;
    }
    public int packetHeaderLength(){
        return this.packetHeaderLength;
    }
    public int maxTimeoutInterval(){return this.maxTimeoutInterval;}
}
