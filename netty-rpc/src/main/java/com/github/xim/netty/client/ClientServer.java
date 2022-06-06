package com.github.xim.netty.client;

import com.eyolo.chat.netty.server.Server;
import com.eyolo.chat.netty.server.ServerConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientServer implements Server {


    private SocketChannel socketChannel;
    private Logger logger= LoggerFactory.getLogger(ClientServer.class);
    @Override
    public boolean start(ServerConfig serverConfig) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(serverConfig.getHost(), serverConfig.getSocketPort())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientHandlerInitilizer());
        ChannelFuture future = bootstrap.connect();
        //客户端断线重连逻辑
        AtomicBoolean isSuccess = new AtomicBoolean(false);
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
              //  log.info("连接Netty服务端成功");
                isSuccess.set(true);
            } else {
              //  log.info("连接失败，进行断线重连");
                future1.channel().eventLoop().schedule(() -> start(serverConfig), 20, TimeUnit.SECONDS);
            }
        });
        socketChannel = (SocketChannel) future.channel();
        return isSuccess.get();
    }

    @Override
    public boolean stop() throws InterruptedException {
        return false;
    }
}
