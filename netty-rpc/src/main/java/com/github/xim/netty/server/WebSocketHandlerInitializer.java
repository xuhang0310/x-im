package com.github.xim.netty.server;
import com.eyolo.chat.netty.handler.WebMessageDispatchHandler;
import com.eyolo.chat.netty.messages.WebSocketDecoder;
import com.eyolo.chat.netty.messages.WebSocketEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


@ChannelHandler.Sharable
public class WebSocketHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ServerConfig serverConfig;

    public WebSocketHandlerInitializer(ServerConfig serverConfig){
        this.serverConfig=serverConfig;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器

        pipeline.addLast(new HttpServerCodec());
        // 支持参数对象解析， 比如POST参数， 设置聚合内容的最大长度
        pipeline.addLast(new HttpObjectAggregator(65536));
        // 支持大数据流写入
        pipeline.addLast(new ChunkedWriteHandler());

        pipeline.addLast(new ServerIdleStateHandler(serverConfig.getServerIdleTime()));
        // 支持WebSocket数据压缩
        pipeline.addLast(new WebSocketServerCompressionHandler());
        // Websocket协议配置， 设置访问路径
        pipeline.addLast(new WebSocketServerProtocolHandler(serverConfig.getWebsocketPath(), null, true));

        pipeline.addLast(new WebSocketDecoder());
        // 协议包编码
        pipeline.addLast(new WebSocketEncoder());
        pipeline.addLast(new WebMessageDispatchHandler());

    }
}
