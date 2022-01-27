package com.amazonaws.components.output;

import com.amazonaws.param.Kinesis;
import com.amazonaws.services.kinesisanalytics.flink.connectors.producer.FlinkKinesisFirehoseProducer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class CustomOrderFirehouseSink {
    public static FlinkKinesisFirehoseProducer<String> create() {
        Properties outputProperties = new Properties();
        outputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Kinesis.region);

        // 部署到你aws kinesis data analytics 以后，无需这个凭证
        // 请注释
        // outputProperties.setProperty(ConsumerConfigConstants.AWS_ACCESS_KEY_ID,
        // Kinesis.accessKey);
        // outputProperties.setProperty(ConsumerConfigConstants.AWS_SECRET_ACCESS_KEY,
        // Kinesis.accessSecret);

        FlinkKinesisFirehoseProducer<String> sink = new FlinkKinesisFirehoseProducer<>("customer_order_ds",
                new SimpleStringSchema(), outputProperties);
        return sink;
    }
}
