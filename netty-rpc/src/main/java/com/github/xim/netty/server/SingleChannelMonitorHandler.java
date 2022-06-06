package com.github.xim.netty.server;

import io.netty.handler.traffic.ChannelTrafficShapingHandler;

/**
 * 单个channel的流量控制
 * @author xupei
 */
public class SingleChannelMonitorHandler extends ChannelTrafficShapingHandler {
    public SingleChannelMonitorHandler(long writeLimit, long readLimit, long checkInterval) {
        super(writeLimit, readLimit, checkInterval);
    }
}
