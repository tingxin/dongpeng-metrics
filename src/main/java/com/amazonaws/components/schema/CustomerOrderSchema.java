package com.amazonaws.components.schema;

import com.amazonaws.bean.CustomerOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;

public class CustomerOrderSchema implements SerializationSchema<CustomerOrder> {

    @Override
    public byte[] serialize(CustomerOrder customerOrder) {
        ObjectMapper jsonParser = new ObjectMapper();
        try {
            return jsonParser.writeValueAsBytes(customerOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}

