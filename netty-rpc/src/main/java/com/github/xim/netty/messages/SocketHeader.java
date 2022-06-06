package com.github.xim.netty.messages;

import java.io.Serializable;

public class SocketHeader implements Serializable {

    //
    /**
     * 起始标记位 对应字符串的值为 EYOL
     */
    private final static int startPerfix=1163480908;
    // 协议版本
    private final  int version;
    // 消息内容长度
    private final  int contentLength;
    // 服务名称
    private  int serviceId;
    //请求的messageId
    private final int messageId;


    public SocketHeader(int version, int contentLength, int serviceId,int messageId) {
        this.version = version;
        this.contentLength = contentLength;
        this.serviceId = serviceId;
        this.messageId=messageId;
    }

    public int getVersion() {
        return version;
    }

    public int getContentLength() {
        return contentLength;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public static int getStartPerfix() {
        return startPerfix;
    }


}
