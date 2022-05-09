package com.amazonaws.components.output;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.components.common.KinesisProps;
import com.amazonaws.components.schema.CustomOrderFirehouseSchema;
import com.amazonaws.components.schema.OrderEventSchema;
import com.amazonaws.param.Kinesis;
import com.amazonaws.services.kinesisanalytics.flink.connectors.producer.FlinkKinesisFirehoseProducer;

import java.util.Properties;

public class OrderEventSink {
    public static FlinkKinesisFirehoseProducer<OrderEvent> firehouse() {
        Properties properties = KinesisProps.outputProperties();
        return new FlinkKinesisFirehoseProducer<>(Kinesis.streamOrderEvent,
                new OrderEventSchema(), properties);
    }
}
