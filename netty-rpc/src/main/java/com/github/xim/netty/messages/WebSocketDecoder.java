package com.github.xim.netty.messages;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

public class WebSocketDecoder extends MessageToMessageDecoder<WebSocketFrame> {

    private static final int minReadLength=20;

    private static final int maxRetry=512;


   // private Logger logger= LoggerFactory.getLogger(WebSocketDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ch, WebSocketFrame frame, List<Object> objs) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            // 文本消息
        } else if (frame instanceof BinaryWebSocketFrame) {
            // 二进制消息
            ByteBuf buf = ((BinaryWebSocketFrame) frame).content();

            decodeByteBuf(buf,objs);

        } else if (frame instanceof PongWebSocketFrame) {
            // PING存活检测消息
        } else if (frame instanceof CloseWebSocketFrame) {
            // 关闭指令消息
            ch.close();
        }
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
//                if(retry<=2){
//                  //  logger.info("remain {} ,contentLength {} ,readerIndex {},wix {} " ,remain,contentLength,in.readerIndex(),in.writerIndex());
//                }
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
