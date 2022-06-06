package com.github.xim.netty.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class SocketEncoder  extends MessageToByteEncoder<SocketMessage> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SocketMessage message, ByteBuf out) throws Exception {

        SocketHeader header = message.getSocketHeader();
        out.writeInt(SocketHeader.getStartPerfix());
        out.writeInt(header.getVersion());
        out.writeInt(message.getContent().length);
        out.writeInt(header.getServiceId());
        if(header.getVersion()!=0){
            out.writeInt(header.getMessageId());
        }
        // 写入消息主体信息
        out.writeBytes(message.getContent());
    }




}
