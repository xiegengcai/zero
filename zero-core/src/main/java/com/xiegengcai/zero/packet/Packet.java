package com.xiegengcai.zero.packet;

/**
 * <pre>数据包定义</pre>
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/24.
 */
public abstract class Packet {

    protected final static int MIN_LENGTH = 10;

    /**
     * 包长度。4字节
     */
    private int length;

    /**
     * 客户端唯一的序列ID， 4字节
     */
    private int sequenceId;
    /**
     * 指令代码。2字节
     */
    private short cmd;
    private byte[] body;

    public abstract int getHeaderLength();


    public int getLength() {
        if (length == 0) {
            return getHeaderLength();
        }
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte [] body) {
        this.body = body;
        if (this.body == null) {
            this.length = getHeaderLength();
        } else {
            this.length = getHeaderLength() + this.body.length;
        }
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
