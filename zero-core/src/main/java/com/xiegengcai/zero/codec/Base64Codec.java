package com.xiegengcai.zero.codec;


import io.netty.channel.Channel;

import java.util.Base64;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
public class Base64Codec implements Codec {

    @Override
    public byte[] decrypt(Channel channel, byte[] body) {
        return Base64.getDecoder().decode(body);
    }

    @Override
    public byte[] encrypt(Channel channel, byte[] body) {
        return Base64.getEncoder().encode(body);
    }
}
