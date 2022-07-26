package com.amazonaws.services.metrics;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class OrderApp {
    StreamExecutionEnvironment env;

    public OrderApp(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public void run() throws Exception {
        String[] t = new String[] {
                "miao,She is a programmer",
                "wu,He is a programmer",
                "zhao,She is a programmer"
        };
        DataStream<String> text = env.fromElements(t);
        text.print();
    }

}
