package com.github.xim.netty.handler;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;

public abstract class BaseProcessor<SocketMessage> {

    public abstract void process(SocketMessage message, ChannelHandlerContext ctx) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
