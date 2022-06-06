package com.github.xim.netty.handler;



import com.github.xim.netty.messages.SocketMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;


@ChannelHandler.Sharable
public class MessageDispatchHandler extends ImHandler<SocketMessage>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketMessage socketMessage) throws Exception {
        if(socketMessage.getSocketHeader().getServiceId()<10000){
            messageProducer.publish(socketMessage, channelHandlerContext);
        }else{

        }

    }


}
