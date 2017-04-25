package com.xiegengcai.zero.server.codec;

import com.xiegengcai.zero.codec.Codec;
import com.xiegengcai.zero.codec.DefaultCodec;
import com.xiegengcai.zero.packet.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ZeroServerEncoder extends MessageToByteEncoder<ResponsePacket> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZeroServerDecoder.class);
    private Codec codec;
    public ZeroServerEncoder() {
        this(new DefaultCodec());
    }
    public ZeroServerEncoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponsePacket packet, ByteBuf out) throws Exception {
        LOGGER.debug("原始包长度:{}", packet.getLength());
        // 加密
        byte []  body = codec.encrypt(ctx.channel(), packet.getBody());
        packet.setBody(body);
        LOGGER.debug("加密后包长度:{}", packet.getLength());
        // 写入包长度
        out.writeInt(packet.getLength());
        // 写入序列ID
        out.writeInt(packet.getSequenceId());
        // 写入指令码
        out.writeShort(packet.getCmd());
        // 写入响应码
        out.writeInt(packet.getCode());
        // 写入body
        if (body != null) {
            out.writeBytes(body);
        }
    }
}
