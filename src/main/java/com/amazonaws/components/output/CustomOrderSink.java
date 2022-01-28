package com.amazonaws.components.output;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.components.schema.CustomerOrderSchema;
import com.amazonaws.param.Kinesis;
import com.amazonaws.services.kinesisanalytics.flink.connectors.producer.FlinkKinesisFirehoseProducer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.io.IOException;
import java.util.Properties;

public class CustomOrderSink {
    private static Properties outputProperties() {
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

    public static FlinkKinesisProducer<String> kinesisStr() throws IOException {
        Properties properties = outputProperties();
        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<String>(
                new SimpleStringSchema(), properties);

        sink.setDefaultStream("metrics-ds");
        sink.setDefaultPartition("0");
        return sink;
    }

    public static FlinkKinesisProducer<CustomerOrder> kinesis() throws IOException {
        Properties properties = outputProperties();
        FlinkKinesisProducer<CustomerOrder> sink = new FlinkKinesisProducer<CustomerOrder>(
                new CustomerOrderSchema(), properties);

        sink.setDefaultStream("metrics-ds");
        sink.setDefaultPartition("0");
        return sink;
    }

    public static FlinkKinesisFirehoseProducer<String> firehouse() {
        Properties properties = outputProperties();
        return new FlinkKinesisFirehoseProducer<>("customer_order_ds",
                new SimpleStringSchema(), properties);
    }
}
