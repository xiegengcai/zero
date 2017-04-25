package com.xiegengcai.zero;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public interface Endpoint {

    void open() throws InterruptedException;

    void close();

    boolean isClosed();

    boolean isAvailable();

}
