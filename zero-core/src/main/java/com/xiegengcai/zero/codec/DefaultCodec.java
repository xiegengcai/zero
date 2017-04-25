package com.xiegengcai.zero.codec;

import io.netty.channel.Channel;

/**
 *     缺省加解密实现
 * <ol>
 *     <li>解码不解密</li>
 *     <li>编码不加密</li>
 * </ol>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public class DefaultCodec implements Codec {

    @Override
    public byte[] decrypt(Channel channel, byte[] body) {
        // 不解密
        return body;
    }

    @Override
    public byte[] encrypt(Channel channel, byte[] body) {
        // 不解密
        return body;
    }
}
