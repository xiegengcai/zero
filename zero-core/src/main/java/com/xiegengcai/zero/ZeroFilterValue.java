package com.xiegengcai.zero;

import com.xiegengcai.zero.filter.Filter;

/**
 * Created by <a href="mailto:xiegengcai@gmail.com">Xie Gengcai</a> on 2016/9/2.
 */
public class ZeroFilterValue implements Comparable<ZeroFilterValue> {
    // 顺序
    private int order;
    private int [] cmds;
    private int [] ignoreCmds;
    private Filter filter;

    public ZeroFilterValue(int order, int[] cmds, int[] ignoreCmds, Filter filter) {
        this.order = order;
        this.cmds = cmds;
        this.ignoreCmds = ignoreCmds;
        this.filter = filter;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public int[] getCmds() {
        return cmds;
    }

    public void setCmds(int[] cmds) {
        this.cmds = cmds;
    }

    public int[] getIgnoreCmds() {
        return ignoreCmds;
    }

    public void setIgnoreCmds(int[] ignoreCmds) {
        this.ignoreCmds = ignoreCmds;
    }

    @Override
    public int compareTo(ZeroFilterValue another) {
        return Integer.compare(this.order, another.order);
    }
}
