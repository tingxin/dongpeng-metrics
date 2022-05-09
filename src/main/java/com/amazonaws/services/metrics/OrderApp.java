package com.amazonaws.services.metrics;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.Metric;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.common.CacheConf;
import com.amazonaws.common.RedshiftConf;
import com.amazonaws.components.async.GetCompanyDetailFunc;
import com.amazonaws.components.input.OrderEventSource;
import com.amazonaws.components.output.*;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;

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
        // 可以通过该方法获取外部传入的参数
        // Map<String, Properties> applicationProperties =
        // KinesisAnalyticsRuntime.getApplicationProperties();
        // Properties runtime = applicationProperties.get("runtime");
        // String inputSourceName = runtime.getProperty("beginTimestampe");

        // set up the streaming execution environment

        DataStream<OrderEvent> input = OrderEventSource.create(env);
//        input = this.addWaterMark(input);

//      DataStream<CustomerOrder> ds = this.attachCustomInfo(input);

        input.addSink(OrderEventSink.firehouse()).name("eventToFirehouse");

//        DataStream<Metric> amountDs = ds.map(item -> new Metric("amount", item.getAmount(), item.getCreateTime()));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        KeyedStream<Metric, String> dailyDs = amountDs.keyBy(item -> formatter.format(item.getOccur()));
//
//        DataStream<Metric> amountMetricDs = dailyDs.reduce(new ReduceFunction<Metric>() {
//            @Override
//            public Metric reduce(Metric t1, Metric t2) throws Exception {
//                Date ts = t1.getOccur().after(t2.getOccur()) ? t1.getOccur() : t2.getOccur();
//                return new Metric(t1.getName(), t1.getValue() + t2.getValue(), ts);
//            }
//        });
//
//        amountMetricDs.addSink(MetricSink.firehouse()).name("metricToFirehouse");
        /* amountMetricDs.print("output"); */
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
                "jdbc:redshift://redshift-cluster-demo.cmnyuhfynqj7.cn-northwest-1.redshift.amazonaws.com.cn:5439/dev");
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
