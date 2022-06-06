package com.github.xim.netty.disruptor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

public class RingBufferWorkPoolFactory {

    private static ThreadFactory threadFactory ;

    private static Disruptor<MessageEvent> disruptor;

    //创建bufferSize ,也就是RingBuffer大小，必须是2的N次方
    private  static  int ringBufferSize = 1024*16; //

    public static Disruptor init( ThreadFactory executors){

        threadFactory=executors;
        //创建工厂
        MessageEventFactory factory = new MessageEventFactory();
        //创建disruptor
        disruptor = new Disruptor<MessageEvent>(factory, ringBufferSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());

        return disruptor;
    }





}
