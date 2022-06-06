package com.github.xim.netty.messages;

public class UnusualByteBufSocket {

    private byte [] readerContent;

    private SocketHeader socketHeader;

    private int readerIndex;

    public byte[] getReaderContent() {
        return readerContent;
    }

    public void setReaderContent(byte[] readerContent) {
        if(this.readerContent ==null){
            this.readerContent =new byte[readerContent.length];
        }
        this.readerContent = readerContent;
    }

    public SocketHeader getSocketHeader() {
        return socketHeader;
    }

    public void setSocketHeader(SocketHeader socketHeader) {
        this.socketHeader = socketHeader;
    }

    public int getReaderIndex() {
        return readerIndex;
    }

    public void setReaderIndex(int readerIndex) {
        this.readerIndex = readerIndex;
    }
}
