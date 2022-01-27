package com.amazonaws.services.metrics;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.common.CacheConf;
import com.amazonaws.common.RedshiftConf;
import com.amazonaws.components.async.GetCompanyDetailFunc;
import com.amazonaws.components.input.OrderEventSource;
import com.amazonaws.components.output.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class OrderApp {
    StreamExecutionEnvironment env;

    public OrderApp(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public void run() throws Exception {
        // 可以通过该方法获取外部传入的参数
        // Map<String, Properties> applicationProperties =
        // KinesisAnalyticsRuntime.getApplicationProperties();
        // Properties runtime = applicationProperties.get("runtime");
        // String inputSourceName = runtime.getProperty("input");

        // set up the streaming execution environment

        DataStream<OrderEvent> input = OrderEventSource.create(env);

        input = this.addWaterMark(input);

        DataStream<CustomerOrder> ds = this.attachCustomInfo(input);

        ObjectMapper jsonParser = new ObjectMapper();
        DataStream<String> bds = ds.map(item -> {
            byte[] bytes = jsonParser.writeValueAsBytes(item);
            return new String(bytes);
        }).disableChaining().name("serialize");
        // bds.print("output");
        bds.addSink(CustomOrderStrSink.create()).disableChaining().name("output");

    }

    DataStream<OrderEvent> addWaterMark(DataStream<OrderEvent> input) {
        // 添加水位线
        WatermarkStrategy<OrderEvent> wmStrategy = WatermarkStrategy
                .<OrderEvent>forMonotonousTimestamps()
                .withTimestampAssigner((event, timestamp) -> event.getCreateTime().getTime());

        return input.assignTimestampsAndWatermarks(wmStrategy).disableChaining().name("add watermark");
    }

    DataStream<CustomerOrder> attachCustomInfo(DataStream<OrderEvent> input) {
        RedshiftConf redshiftConfig = new RedshiftConf();
        redshiftConfig.setJdbcUri(
                "jdbc:redshift://redshift-cluster-weige.cmnyuhfynqj7.cn-northwest-1.redshift.amazonaws.com.cn:5439/dev");
        redshiftConfig.setUserName("admin");
        redshiftConfig.setPassword("Demo1234");
        CacheConf CacheConfig = new CacheConf(Duration.ofDays(1), 10000);

        // 每次请求的超时时间
        int asyncTimeout = 5;
        // 一次能发送几条记录去做异步IO
        int asyncCapacity = 5;
        GetCompanyDetailFunc asyncFunction = new GetCompanyDetailFunc(redshiftConfig, CacheConfig);
        DataStream<CustomerOrder> ds = AsyncDataStream.orderedWait(input, asyncFunction, asyncTimeout, TimeUnit.SECONDS,
                asyncCapacity).disableChaining().name("anyc custom");
        return ds;
    }
}
