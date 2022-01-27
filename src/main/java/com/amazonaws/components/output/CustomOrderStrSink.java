package com.amazonaws.components.output;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.param.Kinesis;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.io.IOException;
import java.util.Properties;

public class CustomOrderStrSink {
    public static FlinkKinesisProducer<String> create() throws IOException {
        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Kinesis.region);

        // 部署到你aws kinesis data analytics 以后，无需这个凭证
        // 请注释
        outputProperties.setProperty(ConsumerConfigConstants.AWS_ACCESS_KEY_ID, Kinesis.accessKey);
        outputProperties.setProperty(ConsumerConfigConstants.AWS_SECRET_ACCESS_KEY, Kinesis.accessSecret);
        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<String>(
                new SimpleStringSchema(),outputProperties);

        sink.setDefaultStream("metrics-ds");
        return sink;
    }
}
