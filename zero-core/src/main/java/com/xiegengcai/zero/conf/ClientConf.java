package com.xiegengcai.zero.conf;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2016-08-25.
 */
public class ClientConf extends Conf{
    @Value("${server.host}")
    private String host;
    @Value("${sever.connect.timeout:3000}")
    private int connectTimeoutMillis;
    public String host() {
        return this.host;
    }

    public int connectTimeoutMillis() {
        return this.connectTimeoutMillis;
    }
}
