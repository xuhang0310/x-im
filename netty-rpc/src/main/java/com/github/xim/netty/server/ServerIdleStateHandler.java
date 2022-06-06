package com.github.xim.netty.server;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author xupei
 */
@ChannelHandler.Sharable
public class ServerIdleStateHandler extends IdleStateHandler {

    public static AbstractIdelStateHandler abstractIdelStateHandler;

    private Logger logger= LoggerFactory.getLogger(ServerIdleStateHandler.class);
    /**
     * 设置空闲检测时间为 30s
     */
    public ServerIdleStateHandler(int serverIdleTimeOut) {
        super(serverIdleTimeOut, serverIdleTimeOut, serverIdleTimeOut*2, TimeUnit.SECONDS);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
       if(abstractIdelStateHandler !=null){
           abstractIdelStateHandler.close(ctx);
       }

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if(abstractIdelStateHandler !=null){
            abstractIdelStateHandler.close(ctx);
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
    }
}
