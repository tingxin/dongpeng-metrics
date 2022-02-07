package com.amazonaws.components.output;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.components.common.KinesisProps;
import com.amazonaws.components.schema.CustomerOrderSchema;
import com.amazonaws.param.Kinesis;
import com.amazonaws.services.kinesisanalytics.flink.connectors.producer.FlinkKinesisFirehoseProducer;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;

import java.io.IOException;
import java.util.Properties;

public class CustomOrderSink {

    public static FlinkKinesisProducer<String> kinesisStr() throws IOException {
        Properties properties = KinesisProps.outputProperties();
        FlinkKinesisProducer<String> sink = new FlinkKinesisProducer<String>(
                new SimpleStringSchema(), properties);

        sink.setDefaultStream(Kinesis.streamDwdCustomerOrder);
        sink.setDefaultPartition("0");
        return sink;
    }

    public static FlinkKinesisProducer<CustomerOrder> kinesis() throws IOException {
        Properties properties = KinesisProps.outputProperties();
        FlinkKinesisProducer<CustomerOrder> sink = new FlinkKinesisProducer<CustomerOrder>(
                new CustomerOrderSchema(), properties);

        sink.setDefaultStream(Kinesis.streamDwdCustomerOrder);
        sink.setDefaultPartition("0");
        return sink;
    }

    public static FlinkKinesisFirehoseProducer<String> firehouse() {
        Properties properties = KinesisProps.outputProperties();
        return new FlinkKinesisFirehoseProducer<>("customer_order_ds",
                new SimpleStringSchema(), properties);
    }
}
