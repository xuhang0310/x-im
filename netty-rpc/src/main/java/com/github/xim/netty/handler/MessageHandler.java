package com.github.xim.netty.handler;


import com.github.xim.netty.disruptor.MessageEvent;
import com.github.xim.netty.messages.SocketMessage;
import com.lmax.disruptor.EventHandler;


public class MessageHandler implements EventHandler<MessageEvent>{

    public static BaseProcessor baseProcess;


    public void onEvent(MessageEvent messageEvent, long l, boolean b) throws Exception {
        SocketMessage message= messageEvent.getObject();
        if(baseProcess!=null){
            baseProcess.process(message,messageEvent.getCtx());
        }
    }

}
