package com.xiegengcai.zero.server.spi;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiegengcai.demo.proto.Message;
import com.xiegengcai.zero.annotation.SPI;
import com.xiegengcai.zero.annotation.ZeroBean;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-03-28.
 */
@ZeroBean
public class UserSerice {

    @SPI(cmd = 100)
    public Message.User login(byte [] body) throws InvalidProtocolBufferException {
        Message.User user = Message.User.newBuilder().mergeFrom(body).build();
        System.out.println("Hello, "+user.getName());
        return user;
    }
}
