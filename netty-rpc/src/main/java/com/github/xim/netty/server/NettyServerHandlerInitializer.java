package com.github.xim.netty.server;

import com.eyolo.chat.netty.handler.MessageDispatchHandler;
import com.eyolo.chat.netty.messages.SocketDecoder;
import com.eyolo.chat.netty.messages.SocketEncoder;
import com.eyolo.chat.netty.messages.ThreadConstant;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {



    private static int M=1024*1024;

    private final ScheduledExecutorService executorService= Executors.newScheduledThreadPool(ThreadConstant.DEFAULT_POLLING_THREAD_COUNT);

    private final FlowMonitoringHandler flowMonitoringHandler=new FlowMonitoringHandler(executorService,300*1024*1024,
            10*1024*1024,50000L,50000L,1000);
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline= ch.pipeline();
        pipeline.addLast(new ServerIdleStateHandler(RemoteNettyServer.serverIdleTimeOut))
                .addLast("monitor",flowMonitoringHandler)
                .addLast(new SocketEncoder())
                .addLast(new SingleChannelMonitorHandler(M,M,1000))
                .addLast(new SocketDecoder())
                .addLast(new MessageDispatchHandler());
    }
}
