package com.github.xim.netty.disruptor;


import com.github.xim.netty.messages.SocketMessage;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {

    private final RingBuffer<MessageEvent> ringBuffer;




    public MessageProducer(RingBuffer<MessageEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }





    public void publish(SocketMessage bb, ChannelHandlerContext ctx){
        //1.可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
        long sequence = ringBuffer.next();
        try {
            //2.用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
            MessageEvent event = ringBuffer.get(sequence);
            if(event!=null){
                //3.获取要通过事件传递的业务数据
                event.setObject(bb);
                event.setCtx(ctx);

            }
        } finally {
            ringBuffer.publish(sequence);
        }
    }


}
