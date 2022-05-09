package com.amazonaws.param;

public class Kinesis {
    public static final String region = "cn-northwest-1";
    public static final String streamDwdCustomerOrder = "dwd_customer_order_ds";
    public static final String streamOrderEvent = "dwd_order_event_ds";
    public static final String streamOrder = "order_ds";
    public static final String streamMetric = "metric_ds";
    // 在本地测试的时候，你访问你的aws服务需要如下凭证，在您的vpc环境是不需要的
    // 请替换，下面的是凭证做参考（下面凭证无效）
    public static final String accessKey = "abcd";
    public static final String accessSecret = "demo";

}
