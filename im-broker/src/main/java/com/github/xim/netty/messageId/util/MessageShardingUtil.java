package com.github.xim.netty.messageId.util;


import com.github.xim.netty.messageId.common.Result;
import com.github.xim.netty.messageId.common.Status;
import com.github.xim.netty.messageId.segment.IDGen;
import com.github.xim.netty.messageId.snowflake.SnowflakeZookeeperHolder;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;

public class MessageShardingUtil implements IDGen {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageShardingUtil.class);
    private static SpinLock mLock = new SpinLock();
    private static int rotateId = 0;
    private static int nodeId = 0;
    private static int workerId=0;
    private final long workerIdBits = 10L;
    private final long maxWorkerId = ~(-1L << workerIdBits);//最大能够分配的workerid =1023

    private static int rotateIdWidth = 15;
    private static int rotateIdMask = 0x7FFF;

    private static int nodeIdWidth = 6;
    private static int nodeIdMask = 0x3F;

    private static final long T201801010000 = 1514736000000L;

    private final long twepoch;



    protected long timeGen() {
        return System.currentTimeMillis();
    }
    private static String host(){
        try{
            InetAddress addr = (InetAddress) InetAddress.getLocalHost();
            String host=addr.getHostAddress().toString();
            return  host;
        }catch (Exception e){

        }
        return null;
    }
    public MessageShardingUtil(String zkAddress, int port) {
        this(zkAddress, port, T201801010000);
    }


    public MessageShardingUtil(String zkAddress, int port, long twepoch) {
        this.twepoch = twepoch;
        Preconditions.checkArgument(timeGen() > twepoch, "Snowflake not support twepoch gt currentTime");
        final String ip =host();
        SnowflakeZookeeperHolder holder = new SnowflakeZookeeperHolder(ip, String.valueOf(port), zkAddress);
        LOGGER.info("twepoch:{} ,ip:{} ,zkAddress:{} port:{}", twepoch, ip, zkAddress, port);
        boolean initFlag = holder.init();
        if (initFlag) {
            workerId = holder.getWorkerID();
            nodeId=workerId;
            LOGGER.info("START SUCCESS USE ZK WORKERID-{}", workerId);
        } else {
            Preconditions.checkArgument(initFlag, "Snowflake Id Gen is not init ok");
        }
        Preconditions.checkArgument(workerId >= 0 && workerId <= maxWorkerId, "workerID must gte 0 and lte 1023");
    }

    /**
     * ID = timestamp(43) + nodeId(6) + rotateId(15)
     * 所以时间限制是到2157/5/15（2的42次幂代表的时间 + (2018-1970)）。节点数限制是小于64，每台服务器每毫秒最多发送32768条消息
     * @return
     */
    private  long generateId() {
        mLock.lock();
        rotateId = (rotateId + 1)&rotateIdMask;
        mLock.unLock();

        long id = System.currentTimeMillis() - T201801010000;


        id <<= nodeIdWidth;
        id += (nodeId & nodeIdMask);

        id <<= rotateIdWidth;
        id += rotateId;
        return id;
    }

    public static String getMessageKeyDate(long mid) {


        Calendar calendar = Calendar.getInstance();
        if (mid != Long.MAX_VALUE) {
            mid >>= (nodeIdWidth + rotateIdWidth);
            Date date = new Date(mid + T201801010000);
            calendar.setTime(date);
            System.out.println(calendar.getTime());
        } else {
            Date date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
        }
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);

        return year+"-"+(month+1)+"-"+day;
    }



    public Result get(String key) {
        return new Result(generateId(), Status.SUCCESS);
    }


    public boolean init() {
        return false;
    }
}
