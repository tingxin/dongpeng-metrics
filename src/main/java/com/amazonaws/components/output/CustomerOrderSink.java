package com.amazonaws.components.output;

import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class CustomerOrderSink {
    public static FlinkKinesisProducer<String> create() throws IOException {
        Map<String, Properties> applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties();
        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<>(new SimpleStringSchema(),
                applicationProperties.get("ProducerConfigProperties"));

        sink.setDefaultStream("metrics-ds");
        sink.setDefaultPartition("0");
        return sink;
    }
}
