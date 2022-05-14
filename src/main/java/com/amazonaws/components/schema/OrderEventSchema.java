package com.amazonaws.components.schema;

import com.amazonaws.bean.Metric;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.services.kinesisanalytics.flink.connectors.serialization.KinesisFirehoseSerializationSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;

import java.nio.ByteBuffer;

public class OrderEventSchema implements KinesisFirehoseSerializationSchema<OrderEvent> {
    ObjectMapper jsonParser;

    public OrderEventSchema() {
        this.jsonParser = new ObjectMapper();
    }

    @Override
    public ByteBuffer serialize(OrderEvent element) {
        try {
            byte[] bytes = this.jsonParser.writeValueAsBytes(element);
            return ByteBuffer.wrap(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}

