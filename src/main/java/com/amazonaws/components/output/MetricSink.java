package com.amazonaws.components.output;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.Metric;
import com.amazonaws.components.schema.CustomerOrderSchema;
import com.amazonaws.components.schema.MetricSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;

import java.io.IOException;
import java.util.Properties;

public class MetricSink {
    public static FlinkKinesisProducer<Metric> kinesis() throws IOException {
        Properties properties = KinesisProps.outputProperties();
        FlinkKinesisProducer<Metric> sink = new FlinkKinesisProducer<Metric>(
                new MetricSchema(), properties);

        sink.setDefaultStream("metric-ds");
        sink.setDefaultPartition("0");
        return sink;
    }
}
