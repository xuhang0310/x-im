package com.github.xim.netty.messageId.snowflake;



import com.github.xim.netty.messageId.common.PropertyFactory;
import com.github.xim.netty.messageId.common.Result;
import com.github.xim.netty.messageId.segment.IDGen;
import com.github.xim.netty.messageId.snowflake.exception.InitException;
import com.github.xim.netty.messageId.util.MessageShardingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service("SnowflakeService")
public class SnowflakeService {

    private Logger logger = LoggerFactory.getLogger(SnowflakeService.class);


    private IDGen idGen;

    public SnowflakeService() throws InitException {
        Properties properties = PropertyFactory.getProperties();
        boolean flag = Boolean.parseBoolean(properties.getProperty(Constants.LEAF_SNOWFLAKE_ENABLE, "true"));
        if (flag) {
            String zkAddress = properties.getProperty(Constants.LEAF_SNOWFLAKE_ZK_ADDRESS);
            int port = Integer.parseInt(properties.getProperty(Constants.LEAF_SNOWFLAKE_PORT));
            idGen= new MessageShardingUtil(zkAddress, port);

        } else {

            logger.info("Zero ID Gen Service Init Successfully");
        }
    }

    public Result getId(String key) {
        return idGen.get(key);
    }

    public static void main(String[] args) throws InitException {
         SnowflakeService snowflakeService=new SnowflakeService();

    }


}
