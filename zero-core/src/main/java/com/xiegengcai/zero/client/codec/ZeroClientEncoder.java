package com.xiegengcai.zero.client.codec;

import com.xiegengcai.zero.codec.Codec;
import com.xiegengcai.zero.codec.DefaultCodec;
import com.xiegengcai.zero.packet.RequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ZeroClientEncoder extends MessageToByteEncoder<RequestPacket> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZeroClientEncoder.class);
    private Codec codec;

    public ZeroClientEncoder() {
        this(new DefaultCodec());
    }

    public ZeroClientEncoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestPacket packet, ByteBuf out) throws Exception {
        LOGGER.debug("原始包长度:{}", packet.getLength());
        // 加密
        byte[] body = codec.encrypt(ctx.channel(), packet.getBody());
        packet.setBody(body);
        LOGGER.debug("加密后包长度:{}", packet.getLength());
        // 写入包长度
        out.writeInt(packet.getLength());
        // 写入序列ID
        out.writeInt(packet.getSequenceId());
        // 写入指令码
        out.writeShort(packet.getCmd());
        // 写入版本号
        out.writeInt(packet.getVersion());

        // 写入body
//        out.writeBytes(packet.getBody().toByteArray());
        if (body != null) {
            out.writeBytes(body);
        }
    }
}
