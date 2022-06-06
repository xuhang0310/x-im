package com.github.xim.netty.messages;
import com.google.protobuf.GeneratedMessageV3;

public class MessageBuilder {

    private static int  version=1;

    public static SocketMessage buildMessage(GeneratedMessageV3 t, int serviceId){
        SocketHeader socketHeader=new SocketHeader(version,t.toByteArray().length,serviceId,0);
        SocketMessage nettyMessage=new SocketMessage(socketHeader,t.toByteArray());
        return nettyMessage;
    }

    public static SocketMessage buildMessage(GeneratedMessageV3 t, int serviceId, int messageId){
        version=1;
        SocketHeader socketHeader=new SocketHeader(version,t.toByteArray().length,serviceId,messageId);
        SocketMessage nettyMessage=new SocketMessage(socketHeader,t.toByteArray());
        return nettyMessage;
    }


}
