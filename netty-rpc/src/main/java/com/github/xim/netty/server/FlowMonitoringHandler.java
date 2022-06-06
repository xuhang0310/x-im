package com.github.xim.netty.server;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author xupei
 */
public class FlowMonitoringHandler extends GlobalChannelTrafficShapingHandler {


    private Logger logger= LoggerFactory.getLogger(FlowMonitoringHandler.class);
    private final Integer M=1024;
    public FlowMonitoringHandler(ScheduledExecutorService executor, long writeGlobalLimit, long readGlobalLimit, long writeChannelLimit, long readChannelLimit, long checkInterval) {
        super(executor, writeGlobalLimit, readGlobalLimit, writeChannelLimit, readChannelLimit, checkInterval);
    }

    @Override
    protected void doAccounting(TrafficCounter counter) {
        super.doAccounting(counter);
        if(counter.getRealWriteThroughput()/M>0){
            logger.info(" flow monitor  {} kb/s, {} kb/s",counter.getRealWriteThroughput()/M,counter.getRealWrittenBytes().get()/M);
        }
        if(counter.currentReadBytes()/M>0){
            logger.info(" flow monitor read {} kb/s , last read {} kb/",counter.currentReadBytes()/M,counter.lastReadBytes()/M);
        }

        if(counter.currentWrittenBytes()/M>0){
            logger.info(" flow monitor write {} kb/s  last read {} kb/s",counter.currentWrittenBytes()/M,counter.lastReadBytes()/M);
        }

    }
}
