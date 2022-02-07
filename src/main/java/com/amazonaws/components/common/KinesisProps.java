package com.amazonaws.components.common;

import com.amazonaws.param.Kinesis;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class KinesisProps {
    public static Properties outputProperties() {
        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Kinesis.region);

        // 部署到你aws kinesis data analytics ，无需这个凭证，请注释
        // 本地开发时候需要
        outputProperties.setProperty(ConsumerConfigConstants.AWS_ACCESS_KEY_ID,
                Kinesis.accessKey);
        outputProperties.setProperty(ConsumerConfigConstants.AWS_SECRET_ACCESS_KEY,
                Kinesis.accessSecret);
        return outputProperties;
    }

}
