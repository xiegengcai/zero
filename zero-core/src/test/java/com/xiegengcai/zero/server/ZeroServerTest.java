package com.xiegengcai.zero.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-03-28.
 */
public class ZeroServerTest {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("zero-server.xml").start();
    }
}
