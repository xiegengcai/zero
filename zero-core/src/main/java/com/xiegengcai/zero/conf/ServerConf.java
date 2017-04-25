package com.xiegengcai.zero.conf;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ServerConf extends Conf{

    @Value("${woker.thread.size:3}")
    private int wokerThreadSize;
    @Value("${service.maxTimeout:60}")
    private int maxTimeout;
    @Value("${service.defaultTimeout:30}")
    private int defaultTimeout;

    public int wokerThreadSize() {
        return this.wokerThreadSize;
    }

    /**
     * 最大超时时间60s,单位秒
     */
    public int maxTimeout() {
        return this.maxTimeout();
    }

    /**
     * 缺省超时时间 30s,单位秒
     */
    public int defaultTimeout() {
        return this.defaultTimeout;
    }

}