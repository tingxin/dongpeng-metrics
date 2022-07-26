package com.amazonaws.services.metrics;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class OrderApp {
    StreamExecutionEnvironment env;

    public OrderApp(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public void run() throws Exception {

        DataStream<String> text = this.env.socketTextStream("localhost", 9999, "\n", 10);

        text.print("hello");
    }

}
