package com.xiegengcai.zero.client;

import com.xiegengcai.zero.Endpoint;

import java.net.InetSocketAddress;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public interface Client extends Endpoint{

    InetSocketAddress remoteAddress();

    /**
     * 发送消息
     * @param cmd
     * @param body
     * @return
     */
    void send(short cmd, byte[] body) throws InterruptedException, Exception;
}
