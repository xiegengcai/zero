package com.xiegengcai.zero.server;

import com.xiegengcai.zero.Endpoint;

import java.net.InetSocketAddress;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public interface Server extends Endpoint{
    InetSocketAddress localAddress();
}
