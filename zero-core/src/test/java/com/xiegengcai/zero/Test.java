package com.xiegengcai.zero;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiegengcai.demo.proto.Message;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-04-20.
 */
public class Test {

    public static void main(String[] args) {
        Message.User user = Message.User.newBuilder().setName("Jacky").setPassword("14rwttet").build();
        try {
            Message.User user2 = Message.User.newBuilder().mergeFrom(user.toByteArray()).build();
            System.out.println(user.equals(user2));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
