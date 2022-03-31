package com.amazonaws.components.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.ByteBuffer;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.services.kinesisanalytics.flink.connectors.serialization.KinesisFirehoseSerializationSchema;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomOrderFirehouseSchema implements KinesisFirehoseSerializationSchema<CustomerOrder> {

    @Override
    public ByteBuffer serialize(CustomerOrder element) {
        ObjectMapper jsonParser = new ObjectMapper();

        try {
            byte[] bytes = jsonParser.writeValueAsBytes(element);
            return ByteBuffer.wrap(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
