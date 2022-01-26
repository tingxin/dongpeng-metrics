package com.amazonaws.components.input;

import com.amazonaws.param.Kinesis;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.util.Properties;

public class OrderEventSource {
    public static DataStream<String> create(StreamExecutionEnvironment env) {
        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Kinesis.region);
        inputProperties.setProperty(ConsumerConfigConstants.AWS_ACCESS_KEY_ID, Kinesis.accessKey);
        inputProperties.setProperty(ConsumerConfigConstants.AWS_SECRET_ACCESS_KEY, Kinesis.accessSecret);
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");

        return env.addSource(new FlinkKinesisConsumer<>("mock-order-event", new SimpleStringSchema(), inputProperties));
    }
}
