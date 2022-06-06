package com.github.xim.netty.handler;



import com.github.xim.netty.disruptor.MessageEvent;
import com.github.xim.netty.disruptor.MessageProducer;
import com.github.xim.netty.disruptor.RingBufferWorkPoolFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ThreadFactory;

public abstract class ImHandler<T>  extends SimpleChannelInboundHandler<T> {

    protected static MessageProducer messageProducer;


    public static void init( ThreadFactory service){

        Disruptor<MessageEvent> disruptor= RingBufferWorkPoolFactory.init(service);

        disruptor.handleEventsWith(new MessageHandler());
        disruptor.start();
        RingBuffer<MessageEvent> ringBuffer = disruptor.getRingBuffer();
        MessageProducer _messageProducer = new MessageProducer(ringBuffer);
        messageProducer=_messageProducer;
    }
}
