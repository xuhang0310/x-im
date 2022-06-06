package com.github.xim.netty.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

public class WebSocketEncoder extends MessageToMessageEncoder<SocketMessage> {

    public byte[] intToByte4B(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n >> 24 & 0xff); //数据组起始位,存放内存起始位, 即:高字节在前
        b[1] = (byte) (n >> 16 & 0xff); //高字节在前是与java存放内存一样的, 与书写顺序一样
        b[2] = (byte) (n >> 8 & 0xff);
        b[3] = (byte) (n & 0xff);
        return b;
    }



    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SocketMessage msg, List<Object> out) throws Exception {
        ByteBuf result = null;
        int startPerfix=SocketHeader.getStartPerfix();
        int version= msg.getSocketHeader().getVersion();
        int length=msg.getContent().length;
        int serviceId= msg.getSocketHeader().getServiceId();
        int messageId=msg.getSocketHeader().getMessageId();
        byte [] bStartPerfix= intToByte4B(startPerfix);
        byte [] bVersion= intToByte4B(version);
        byte [] bLength= intToByte4B(length);
        byte [] bServiceId= intToByte4B(serviceId);

        byte [] bMessageId= intToByte4B(messageId);
        int byMessageLength=bMessageId.length;

        if(version==0){
            byMessageLength=0;
        }

        byte[] bt3 = new byte[bStartPerfix.length+bVersion.length+bLength.length+bServiceId.length+bMessageId.length+msg.getContent().length];

        System.arraycopy(bStartPerfix, 0, bt3, 0, bStartPerfix.length);
        System.arraycopy(bVersion, 0, bt3, bStartPerfix.length, bVersion.length);
        System.arraycopy(bLength, 0, bt3, bVersion.length+bStartPerfix.length, bLength.length);
        System.arraycopy(bServiceId, 0, bt3, bVersion.length+bLength.length+bStartPerfix.length, bServiceId.length);

        System.arraycopy(bMessageId, 0, bt3, bVersion.length+bLength.length+bServiceId.length+bStartPerfix.length, bMessageId.length);

        System.arraycopy(msg.getContent(), 0, bt3, bVersion.length+bLength.length+bServiceId.length+byMessageLength+bStartPerfix.length, msg.getContent().length);
        result=wrappedBuffer(bt3);
        WebSocketFrame frame = new BinaryWebSocketFrame(result);
        out.add(frame);
    }


}