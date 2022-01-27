package com.amazonaws.components.input;

import com.amazonaws.bean.OrderEvent;
import com.amazonaws.param.Kinesis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

public class OrderEventSource {
    public static DataStream<OrderEvent> create(StreamExecutionEnvironment env) {
        Properties inputProperties = new Properties();
        inputProperties.setProperty(ConsumerConfigConstants.AWS_REGION, Kinesis.region);
        inputProperties.setProperty(ConsumerConfigConstants.STREAM_INITIAL_POSITION, "LATEST");

        // 部署到你aws kinesis data analytics 以后，无需这个凭证
        // 请注释
        // inputProperties.setProperty(ConsumerConfigConstants.AWS_ACCESS_KEY_ID,
        // Kinesis.accessKey);
        // inputProperties.setProperty(ConsumerConfigConstants.AWS_SECRET_ACCESS_KEY,
        // Kinesis.accessSecret);

        DataStream<String> strDs = env
                .addSource(new FlinkKinesisConsumer<>("mock-order-event", new SimpleStringSchema(), inputProperties));

        ObjectMapper jsonParser = new ObjectMapper();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        DataStream<OrderEvent> event = strDs.map(new MapFunction<String, OrderEvent>() {
            @Override
            public OrderEvent map(String s) throws Exception {
                JsonNode jsonNode = jsonParser.readValue(s, JsonNode.class);
                OrderEvent item = new OrderEvent();
                item.setUserMail(jsonNode.get("user_mail").asText());
                item.setStatus(jsonNode.get("status").asText());
                item.setGoodCount(jsonNode.get("good_count").asInt());
                item.setCity(jsonNode.get("city").asText());
                item.setAmount(jsonNode.get("amount").asDouble());

                String strCreateTime = jsonNode.get("create_time").asText();
                String strUpdateTime = jsonNode.get("update_time").asText();
                item.setCreateTime(formatter.parse(strCreateTime));
                item.setUpdateTime(formatter.parse(strUpdateTime));
                return item;
            }
        }).disableChaining().name("input event");
        return event;
    }
}
