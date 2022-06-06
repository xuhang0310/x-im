package com.github.xim.netty.server;


import com.eyolo.chat.netty.messages.ThreadConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class WebsocketServer  implements Server {


    private Logger logger=LoggerFactory.getLogger(this.getClass());


    private EventLoopGroup boss;

    private EventLoopGroup work;




    public boolean start(ServerConfig config)  {
        if(!config.getSocketType().equals(ServerConfig.SocketEnum.WEBSOCKET)){
            return false;
        }
        if(config.getSocketPort().compareTo(0)==0){
            return false;
        }

        ServerBootstrap bootStrap=  getBootstrap();
        boss=getBoss();
        work=getWork();
        try{
            bootStrap.group(boss, work)
                    // 指定Channel
                    .channel(getChannelClass())
                    //使用指定的端口设置套接字地址
                    .localAddress(new InetSocketAddress(config.getSocketPort()))
                    .childHandler(new WebSocketHandlerInitializer(config));
            ChannelFuture future = bootStrap.bind().sync();
            if (future.isSuccess()) {
                logger.info("WebSocket启用成功");
                return  true;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            return  false;
        }
        return false;
    }

    @Override
    public boolean stop() throws InterruptedException {
       boss.shutdownGracefully().sync();
       work.shutdownGracefully().sync();
       return  true;
    }


    private Class getChannelClass(){
        if(Epoll.isAvailable()){
            return EpollServerSocketChannel.class;
        }
        return NioServerSocketChannel.class;
    }


    private EventLoopGroup getBoss() {
        if(Epoll.isAvailable()){
            return new EpollEventLoopGroup(1);
        }
        return new NioEventLoopGroup(1);
    }

    private EventLoopGroup getWork() {
        if(Epoll.isAvailable()){
            return new EpollEventLoopGroup(ThreadConstant.DEFAULT_POLLING_THREAD_COUNT);
        }
        return new NioEventLoopGroup(ThreadConstant.DEFAULT_POLLING_THREAD_COUNT);
    }

    public ServerBootstrap getBootstrap(){
        return new ServerBootstrap();
    }


}
