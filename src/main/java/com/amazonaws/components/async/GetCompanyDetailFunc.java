package com.amazonaws.components.async;

import com.amazonaws.bean.Customer;
import com.amazonaws.bean.CustomerOrder;
import com.amazonaws.bean.OrderEvent;
import com.amazonaws.common.AbstractAsyncWithCacheFunction;
import com.amazonaws.common.CacheConf;
import com.amazonaws.common.RedshiftConf;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.functions.async.ResultFuture;

import java.util.*;


public class GetCompanyDetailFunc extends AbstractAsyncWithCacheFunction<OrderEvent, CustomerOrder, Customer> {
        private static final Logger LOGGER = LoggerFactory.getLogger(GetCompanyDetailFunc.class);
        private static final String SQLSTS = "SELECT sex, level FROM customer where email = '%s'";


        private static final String METRIC_NAME_ASYNC_CALL_SUCCESS_COUNT = "metric.async.GetCompanyDetailFunc.success";
        private static final String METRIC_NAME_ASYNC_CALL_FAIL_COUNT = "metric.async.GetCompanyDetailFunc.fail";

        private transient Counter successCounter;
        private transient Counter failCounter;


        public GetCompanyDetailFunc(RedshiftConf conf, CacheConf cacheConf) {
                super(conf, cacheConf);
        }

        @Override
        public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                super.open(parameters);
                successCounter = getRuntimeContext()
                        .getMetricGroup()
                        .counter(METRIC_NAME_ASYNC_CALL_SUCCESS_COUNT);
                failCounter = getRuntimeContext()
                        .getMetricGroup()
                        .counter(METRIC_NAME_ASYNC_CALL_FAIL_COUNT);
        }

        @Override
        protected String buildSQL(OrderEvent input) {
                return String.format(SQLSTS, input.getUserMail());
        }

        @Override
        protected Collection<CustomerOrder> buildOutMessage(OrderEvent input, Map<String, Customer> cachedRecord) {
                Customer c = cachedRecord.get(input.getUserMail());
                CustomerOrder co = new CustomerOrder(c, input);
                return Collections.singletonList(co);
        }

        @Override
        protected Pair<Boolean, Map<String, Customer>> getCachedRecord(OrderEvent input) {

                Map<String, Customer> cacheMap = getCache().getAllPresent(Collections.singletonList(input.getUserMail()));
                if (cacheMap.isEmpty()) {
                        return Pair.of(false, null);
                }

                return Pair.of(true, cacheMap);
        }

        @Override
        protected void handleSuccess(OrderEvent input, ResultFuture<CustomerOrder> output, ResultSet resultSet) {

                for (JsonObject row : resultSet.getRows()) {
                        String sex = row.getString("sex");
                        int level = row.getInteger("level");

                        Customer m = new Customer(input.getUserMail(), sex,level);
                        CustomerOrder co = new CustomerOrder(m, input);

                        output.complete(Collections.singletonList(co));
                        getCache().put(input.getUserMail(), m);

                        // 目前假设的场景是，一个用户user_mail,在用户表中只能查找到一条信息，所以，直接break

                        break;
                }
                successCounter.inc();

//                // 如果存在一对多的情形需要适当修改 应该这么写
//
//                List<CustomerOrder> target = new ArrayList<>();
//                for (JsonObject row : resultSet.getRows()) {
//                        String sex = row.getString("sex");
//                        String level = row.getString("level");
//
//                        Customer m = new Customer(input.getUserMail(), sex,level);
//                        getCache().put(input.getUserMail(), m);
//
//                        CustomerOrder co = new CustomerOrder(m, input);
//                        target.add(co);
//                        successCounter.inc();
//                }
//                output.complete(target);
        }

        @Override
        protected void handleFailure(OrderEvent input, ResultFuture<CustomerOrder> output) {

        }
}
