package com.xiegengcai.zero.packet;

/**
 * <pre>请求数据包</pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/18.NS
 */
public class RequestPacket extends Packet {
    protected final static int HEADER_LENGTH = Packet.MIN_LENGTH + 4;
    /**
     * 指令版本号，4字节
     */
    private int version;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int getHeaderLength() {
        return HEADER_LENGTH;
    }

}
