package com.amazonaws.components.input;

import com.amazonaws.bean.OrderEvent;
import com.amazonaws.components.common.KinesisProps;
import com.amazonaws.help.OrderEventHelper;
import com.amazonaws.param.Kinesis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

public class OrderEventSource {
    public static DataStream<OrderEvent> create(StreamExecutionEnvironment env) {
        Properties properties = KinesisProps.outputProperties();


        DataStream<String> strDs = env
                .addSource(new FlinkKinesisConsumer<>(Kinesis.streamOrder, new SimpleStringSchema(), properties));

        ObjectMapper jsonParser = new ObjectMapper();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        DataStream<OrderEvent> event = strDs.process(new ProcessFunction<String, OrderEvent>() {

            @Override
            public void processElement(String s, Context context, Collector<OrderEvent> collector) throws Exception {
                JsonNode jsonNode = jsonParser.readValue(s, JsonNode.class);
                String tableName = jsonNode.get("table").asText();
                String dbName = jsonNode.get("database").asText();
                String sourcePath = String.format("%s.%s", dbName, tableName);
                switch (sourcePath) {
                    case "demo.order":
                        JsonNode orderTable = jsonNode.get("data");
                        OrderEvent item = OrderEventHelper.createBy(orderTable, formatter);
                        collector.collect(item);
                        break;
                    /*case "demo.otherYourNeededTable":
                        // 使用边侧输出获取其他数据流
                        // 参考 https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/datastream/side_output/
                        context.output();*/
                    default:
                        break;

                }

            }
        }).disableChaining().name("input event");
        return event;
    }
}
