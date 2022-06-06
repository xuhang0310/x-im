package com.github.xim.netty.disruptor;

import com.lmax.disruptor.EventFactory;

public class MessageEventFactory implements EventFactory {

    public Object newInstance() {
        return new MessageEvent();
    }
}
