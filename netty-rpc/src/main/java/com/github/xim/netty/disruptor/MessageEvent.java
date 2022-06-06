package com.github.xim.netty.disruptor;


import com.github.xim.netty.messages.SocketMessage;
import io.netty.channel.ChannelHandlerContext;

public class MessageEvent{

    public SocketMessage object;

    private ChannelHandlerContext ctx;

    public void setObject(SocketMessage object) {
        this.object = object;
    }

    public SocketMessage getObject() {
        return object;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
