package com.github.xim.netty.client;

import io.netty.channel.Channel;

public abstract class BaseClientHandler<T> {

    public  abstract void doClient(T message, Channel channel);
}
