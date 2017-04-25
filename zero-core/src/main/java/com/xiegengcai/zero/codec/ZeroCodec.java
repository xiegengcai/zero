package com.xiegengcai.zero.codec;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">xiegengcai</a> on 2017-03-28.
 */
public class ZeroCodec implements Codec {

    @Value("${zero.secure.secret}")
    private String secret;

    @Override
    public byte[] decrypt(Channel channel, byte[] bytes) {
        return encrypt(getSecret(channel), bytes);
    }

    @Override
    public byte[] encrypt(Channel channel, byte[] bytes) {
        return encrypt(getSecret(channel), bytes);
    }

    private byte [] getSecret(Channel channel) {
        return this.secret.getBytes();
    }

    /**
     * 使用key来对明文进行逐字节进行异或。
     * <ol>
     *     <li>如果明文比key长，异或到最后一个字节后，又从key的第一个字节开始对明文进行异或，如此循环。直到全部明文异或完成。</li>
     *     <li>如果key比明文长，那么就使用key的前面明文长度的字节与明文异或。</li>
     * </ol>
     * @param keys
     * @param body
     * @return
     */
    private byte[] encrypt(byte [] keys, byte [] body) {
        Assert.noNullElements(new Object[]{keys, body}, "秘钥keys及内容body皆不能为空。");
        byte[] result = new byte[body.length];
        int index = 0;
        for (int i = 0; i < body.length; i++) {
            if (i >= keys.length) {
                index = 0;
            }
            result[i] = (byte) (body[i] ^ keys[index++]);
        }
        return result;
    }
}
