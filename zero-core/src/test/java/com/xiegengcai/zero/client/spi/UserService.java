package com.xiegengcai.zero.client.spi;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xiegengcai.demo.proto.Message;
import com.xiegengcai.zero.annotation.SPI;
import com.xiegengcai.zero.annotation.ZeroBean;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-04-20.
 */
@ZeroBean
public class UserService {
    @SPI(cmd = 100)
    public Message.User login(byte [] body) throws InvalidProtocolBufferException {
        Message.User user = Message.User.newBuilder().mergeFrom(body).build();
        System.out.println("Hello, "+user.getName());
        return user;
    }
}
