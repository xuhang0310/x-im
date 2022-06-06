package com.github.xim.netty.server;


import com.eyolo.chat.netty.messages.ThreadConstant;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author xupei
 */
public class RemoteNettyServer implements Server{
    private EventLoopGroup boss;
    private EventLoopGroup work;
    private Logger logger= LoggerFactory.getLogger(RemoteNettyServer.class);
    private static final NettyServerHandlerInitializer nettyServerHandlerInitializer=new NettyServerHandlerInitializer();

    public static Integer serverIdleTimeOut=60;

    /**
     * 启动Netty Server
     *
     * @throws InterruptedException
     */
    @Override
    public boolean start(ServerConfig serverConfig)  {
        if(!serverConfig.getSocketType().equals(ServerConfig.SocketEnum.SOCKET)){
            return false;
        }
        if(serverConfig.getSocketPort().compareTo(0)==0){
            return false;
        }
        if(serverConfig.getServerIdleTime()>serverIdleTimeOut){
            serverIdleTimeOut=serverConfig.getServerIdleTime();
        }

           ServerBootstrap bootStrap=  getBootstrap();
           boss=getBoss();
           work=getWork();
            try{
                bootStrap.group(boss, work)
                        .channel(getChannelClass())
                        .localAddress(new InetSocketAddress(serverConfig.getSocketPort()))
                        .option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
                        .childOption(ChannelOption.SO_REUSEADDR,true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childOption(ChannelOption.ALLOCATOR,new PooledByteBufAllocator(true))
                        .childHandler(nettyServerHandlerInitializer);

                ChannelFuture future = bootStrap.bind().sync();

                if (future.isSuccess()) {
                    logger.info("启动 Netty Server");
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }finally {

            }

           return false;

    }

    @Override
    public boolean stop() throws InterruptedException {
        boss.shutdownGracefully().sync();
        work.shutdownGracefully().sync();
        logger.info("关闭Netty");
        return true;
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
