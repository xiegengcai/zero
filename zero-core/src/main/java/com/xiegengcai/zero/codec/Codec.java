package com.xiegengcai.zero.codec;

import io.netty.channel.Channel;

/**
 * <pre>
 *     加解密接口
 * </pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/25.
 */
public interface  Codec {

    /**
     * 将报文body部分解密
     * @param channel io连接通道信息
     * @param body
     * @return
     */
    byte[] decrypt(Channel channel, byte[] body);

    /**
     * 将响应body部分加密
     * @param channel io连接通道信息
     * @param body
     * @return
     */
    byte[] encrypt(Channel channel, byte[] body);
}
