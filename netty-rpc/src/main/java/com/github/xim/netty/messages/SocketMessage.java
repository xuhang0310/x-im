package com.github.xim.netty.messages;

import java.io.Serializable;

public class SocketMessage implements Serializable {

    private final SocketHeader socketHeader;
    private final byte [] content;

    public SocketMessage(SocketHeader socketHeader, byte [] content) {
        this.socketHeader = socketHeader;
        this.content = content;
    }

    public SocketHeader getSocketHeader() {
        return socketHeader;
    }

    public byte[] getContent() {
        return content;
    }


}
