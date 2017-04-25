package com.xiegengcai.zero.packet;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public class ResponsePacket extends Packet {
    protected final static int HEADER_LENGTH = Packet.MIN_LENGTH + 4;

    // 响应码。4字节
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int getHeaderLength() {
        return HEADER_LENGTH;
    }
}
