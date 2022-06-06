package com.github.xim.netty.server;

public class ServerConfig {
    private String host;
    private ServerType serverType;
    private SocketEnum socketType;
    private Integer socketPort;
    private String websocketPath;
    private Integer serverIdleTime=60;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getSocketPort() {
        return socketPort;
    }

    public void setSocketPort(Integer socketPort) {
        this.socketPort = socketPort;
    }


    public enum SocketEnum{
        SOCKET(0, "SOCKET"),
        WEBSOCKET(1, "WEBSOCKET");



        private Integer code;

        private String desc;

        SocketEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public enum ServerType{
        SEVER(0, "Server"),
        CLIENT(1, "Client");



        private Integer code;

        private String desc;

        ServerType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public SocketEnum getSocketType() {
        return socketType;
    }

    public void setSocketType(SocketEnum socketType) {
        this.socketType = socketType;
    }

    public String getWebsocketPath() {
        return websocketPath;
    }

    public void setWebsocketPath(String websocketPath) {
        if(!websocketPath.startsWith("/")){
           this.websocketPath="/"+websocketPath;
        }else{
            this.websocketPath = websocketPath;
        }

    }

    public Integer getServerIdleTime() {
        return serverIdleTime;
    }

    public void setServerIdleTime(Integer serverIdleTime) {
        this.serverIdleTime = serverIdleTime;
    }
}
