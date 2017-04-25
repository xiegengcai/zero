package com.xiegengcai.zero;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/8/26.
 */
public class SpiMethodValue {

    private int cmd;
    private byte version;
    /**
     * 服务方法是否已经过期,默认不过期
     */
    private boolean obsoleted;

    public SpiMethodValue() {
    }

    public SpiMethodValue(int cmd, byte version, boolean obsoleted) {
        this.cmd = cmd;
        this.version = version;
        this.obsoleted = obsoleted;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public boolean isObsoleted() {
        return obsoleted;
    }

    public void setObsoleted(boolean obsoleted) {
        this.obsoleted = obsoleted;
    }
}
