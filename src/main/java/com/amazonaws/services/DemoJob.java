package com.amazonaws.services;


import com.amazonaws.services.metrics.OrderApp;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * A basic Kinesis Data Analytics for Java application with Kinesis data
 * streams as source and sink.
 */
public class DemoJob {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        OrderApp app = new OrderApp(env);
        app.run();

        env.execute("Flink Streaming Java API Skeleton");
    }

}
