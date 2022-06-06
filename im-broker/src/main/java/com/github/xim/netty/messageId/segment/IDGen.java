package com.github.xim.netty.messageId.segment;


import com.github.xim.netty.messageId.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
