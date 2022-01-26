package com.amazonaws.services.metrics;

import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.common.CacheConf;
import com.amazonaws.common.MysqlConf;
import com.amazonaws.common.RedshiftConf;
import com.amazonaws.components.async.GetCompanyDetailFunc;
import com.amazonaws.components.input.OrderEventSource;
import com.amazonaws.services.kinesisanalytics.runtime.KinesisAnalyticsRuntime;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisConsumer;
import org.apache.flink.streaming.connectors.kinesis.FlinkKinesisProducer;
import org.apache.flink.streaming.connectors.kinesis.config.ConsumerConfigConstants;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.time.Duration;

/**
 * A basic Kinesis Data Analytics for Java application with Kinesis data
 * streams as source and sink.
 */
public class DemoJob {

    public static void main(String[] args) throws Exception {
        // 可以通过该方法获取外部传入的参数
//        Map<String, Properties> applicationProperties = KinesisAnalyticsRuntime.getApplicationProperties();
//        Properties runtime = applicationProperties.get("runtime");
//        String inputSourceName = runtime.getProperty("input");

        // set up the streaming execution environment
        System.out.print(com.amazon.redshift.jdbc.Driver.class.getCanonicalName());
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<OrderEvent> input = OrderEventSource.create(env);


        RedshiftConf redshiftConfig = new RedshiftConf();
        redshiftConfig.setJdbcUri("jdbc:redshift://redshift-cluster-weige.cmnyuhfynqj7.cn-northwest-1.redshift.amazonaws.com.cn:5439/dev");
        redshiftConfig.setUserName("admin");
        redshiftConfig.setPassword("Demo1234");
        CacheConf CacheConfig = new CacheConf(Duration.ofDays(1), 10000);

        // 每次请求的超时时间
        int asyncTimeout = 5;
        // 一次能发送几条记录去做异步IO
        int asyncCapacity = 5;
        GetCompanyDetailFunc asyncFunction =new GetCompanyDetailFunc(redshiftConfig, CacheConfig);
        DataStream<CustomerOrder> ds =  AsyncDataStream.orderedWait(input, asyncFunction, asyncTimeout, TimeUnit.SECONDS, asyncCapacity);


        ds.print("toStd");

        env.execute("Flink Streaming Java API Skeleton");
    }
}
