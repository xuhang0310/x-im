package com.github.xim.netty.handler;

import com.github.xim.netty.messages.SocketMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;


@ChannelHandler.Sharable
public class WebMessageDispatchHandler extends ImHandler<SocketMessage>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketMessage socketMessage) throws Exception {
       //System.out.println("----dsafdsafdsa");
        messageProducer.publish(socketMessage, channelHandlerContext);
    }


}
