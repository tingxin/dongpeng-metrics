package com.amazonaws.components.schema;

import com.amazonaws.bean.Metric;
import com.amazonaws.bean.OrderEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class OrderEventSchema implements SerializationSchema<OrderEvent> {

    @Override
    public byte[] serialize(OrderEvent t) {
        ObjectMapper jsonParser = new ObjectMapper();
        try {
            return jsonParser.writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}

