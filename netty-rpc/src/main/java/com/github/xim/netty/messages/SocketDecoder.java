package com.github.xim.netty.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


import java.util.List;


public class SocketDecoder  extends ByteToMessageDecoder {


    private static final int minReadLength=20;

    private static final int maxRetry=512;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        decodeByteBuf(in,out);
    }



    private void decodeByteBuf(ByteBuf in, List<Object> out){
        int retry=0;
        boolean isCheckHeader=true;
        int version=0;
        int contentLength=0;
        int serviceId=0;
        int messageId=0;
        int remain=0;
        while (true){
            if (in.readableBytes() < minReadLength) {
               // logger.info("< minReadLength");
                return;
            }
            if(isCheckHeader){
                int startPrefix=in.readInt(); // 开头前缀
                if(SocketHeader.getStartPerfix()!=startPrefix){
                    return;
                }
                version = in.readInt(); // 版本号
                contentLength = in.readInt(); // 获取消息长度
                serviceId=in.readInt(); // 信令编码
                messageId=in.readInt(); // 客户端自定义messageId
            }
            remain=in.writerIndex()-in.readerIndex();
            if(remain>=contentLength){
                SocketHeader header = new SocketHeader(version, contentLength, serviceId,messageId);
                decodeByteBufAndWriteToOut(in,out,header);
                remain=in.writerIndex()-in.readerIndex();
                isCheckHeader=true;
                in.markReaderIndex();
                break;
            }else if(remain<contentLength){
                retry++;
                if(retry>maxRetry){
                    break;
                }
                isCheckHeader=false;
                in.resetReaderIndex();
                in.markReaderIndex();
            }
        }

    }

    private void decodeByteBufAndWriteToOut(ByteBuf in, List<Object> out, SocketHeader socketHeader){
        // 组装协议头
        ByteBuf bodyByteBuf = in.readBytes(socketHeader.getContentLength());
        byte[] content;
        int readableLen= bodyByteBuf.readableBytes();
        if (bodyByteBuf.hasArray()) {
            content = bodyByteBuf.array();
        } else {
            content = new byte[readableLen];
            bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), content, 0, readableLen);
        }
        SocketMessage message = new SocketMessage(socketHeader, content);
        bodyByteBuf.release();
        out.add(message);
    }


}
