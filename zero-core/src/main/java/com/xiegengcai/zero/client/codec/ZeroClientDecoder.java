package com.xiegengcai.zero.client.codec;

import com.xiegengcai.zero.codec.Codec;
import com.xiegengcai.zero.codec.DefaultCodec;
import com.xiegengcai.zero.packet.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ZeroClientDecoder extends ByteToMessageDecoder {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZeroClientDecoder.class);
    private Codec codec;

    public ZeroClientDecoder() {
        this.codec = new DefaultCodec();
    }
    public ZeroClientDecoder(Codec codec) {
        this.codec = codec;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        ResponsePacket packet = new ResponsePacket();
        int packetLen = byteBuf.readInt();
        LOGGER.debug("原始包长度：{}", packetLen);
        // 设置序列ID
        packet.setSequenceId(byteBuf.readInt());
        // 设置指令代码
        packet.setCmd(byteBuf.readShort());
        // 设置响应码
        packet.setCode(byteBuf.readInt());
        byte [] tytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(tytes);
        // 解密
        packet.setBody(codec.decrypt(ctx.channel(), tytes));
        // 解密后长度
        packetLen = packet.getLength();
        LOGGER.debug("解密后包长度：{}", packetLen);
        // 重置包长度
        packet.setLength(packetLen);
        list.add(packet);
    }

}
