package com.github.xim.netty.server;

public interface Server {

    public boolean start(ServerConfig serverConfig) ;

    public boolean stop() throws InterruptedException;
}
