package com.amazonaws.components.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.ByteBuffer;

import com.amazonaws.bean.Metric;
import com.amazonaws.services.kinesisanalytics.flink.connectors.serialization.KinesisFirehoseSerializationSchema;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MetricFirehouseSchema implements KinesisFirehoseSerializationSchema<Metric> {
    ObjectMapper jsonParser;
    public MetricFirehouseSchema() {
        this.jsonParser =  new ObjectMapper();
    }

    @Override
    public ByteBuffer serialize(Metric element) {

        try {
            byte[] bytes = this.jsonParser.writeValueAsBytes(element);
            return ByteBuffer.wrap(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
