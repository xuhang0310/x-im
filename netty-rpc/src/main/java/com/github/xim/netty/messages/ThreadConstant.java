package com.github.xim.netty.messages;

public class ThreadConstant {

    public static final int DEFAULT_POLLING_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1 ?
            Runtime.getRuntime().availableProcessors() / 2 : 1;

}
