package com.xiegengcai.zero.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-03-28.
 */
public class ZeroClientTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("zero-client.xml");
        context.start();
    }
}
