package com.github.xim.netty.client;

import com.eyolo.chat.netty.messages.SocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ClientMessageDispatchHandler extends SimpleChannelInboundHandler<SocketMessage> {

    private Logger logger=LoggerFactory.getLogger(ClientMessageDispatchHandler.class);

    private static HashMap<Integer,BaseClientHandler> clientHandlerHashMap=new HashMap<>();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SocketMessage socketMessage) throws Exception {
       // logger.info("serverId {} contentLength {} ",socketMessage.getSocketHeader().getServiceId()+":",socketMessage.getSocketHeader().getContentLength());

        BaseClientHandler clientHandler= clientHandlerHashMap.get(socketMessage.getSocketHeader().getServiceId());
        if(clientHandler!=null){
            clientHandler.doClient(socketMessage.getContent(),channelHandlerContext.channel());
        }

    }

    public static void put(Integer serviceId, BaseClientHandler clientHandler){
        clientHandlerHashMap.put(serviceId,clientHandler);
    }
}
