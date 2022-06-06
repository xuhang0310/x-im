package com.github.xim.netty.client;


import com.github.xim.netty.messages.SocketDecoder;
import com.github.xim.netty.messages.SocketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientHandlerInitilizer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
                .addLast(new IdleStateHandler(0, 15, 0))
                .addLast(new SocketDecoder())
                .addLast(new SocketEncoder());
        ch.pipeline().addLast(new ClientMessageDispatchHandler());



    }
}
