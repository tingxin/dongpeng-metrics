package com.amazonaws.components.output;

import com.amazonaws.bean.Metric;
import com.amazonaws.components.common.KinesisProps;
import com.amazonaws.components.schema.MetricFirehouseSchema;
import com.amazonaws.components.schema.MetricSchema;
import com.amazonaws.param.Kinesis;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import com.amazonaws.services.kinesisanalytics.flink.connectors.producer.FlinkKinesisFirehoseProducer;
import java.io.IOException;
import java.util.Properties;

public class MetricSink {
    public static FlinkKinesisProducer<Metric> kinesis() throws IOException {
        Properties properties = KinesisProps.outputProperties();
        FlinkKinesisProducer<Metric> sink = new FlinkKinesisProducer<>(new MetricSchema(), properties);

        sink.setDefaultStream(Kinesis.streamMetric);
        sink.setDefaultPartition("0");
        return sink;
    }

    public static FlinkKinesisFirehoseProducer<Metric> firehouse() {
        Properties properties = KinesisProps.outputProperties();
        return new FlinkKinesisFirehoseProducer<Metric>(Kinesis.streamMetric,
                new MetricFirehouseSchema(), properties);
    }
}
