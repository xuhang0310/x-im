package com.github.xim.netty.server;
import io.netty.channel.ChannelHandlerContext;

public abstract class AbstractIdelStateHandler implements Cloneable {
    public abstract void close(ChannelHandlerContext ctx);
}
