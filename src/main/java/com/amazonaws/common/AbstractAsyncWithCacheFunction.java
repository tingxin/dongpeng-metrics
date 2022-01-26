package com.amazonaws.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.FileSystemOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.Collection;
import java.util.Map;


public abstract class AbstractAsyncWithCacheFunction<I, O, C> extends RichAsyncFunction<I, O> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAsyncWithCacheFunction.class);
    private final DBConf dbConf;
    private final CacheConf cacheConfig;
    private transient SQLClient sqlClient;
    private transient volatile Cache<String, C> cache;

    public AbstractAsyncWithCacheFunction(DBConf dbConf, CacheConf cacheConfig) {
        super();
        this.cacheConfig = cacheConfig;
        this.dbConf = dbConf;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        init();

    }

    public SQLClient getSqlClient() {
        return sqlClient;
    }

    public Cache<String, C> getCache() {
        return cache;
    }

    private void init() {
        sqlClient = createSQLClient(this.dbConf);

        cache = Caffeine.newBuilder()
                .expireAfterWrite(cacheConfig.getWriteExpireDuration())
                .maximumSize(cacheConfig.getMaxSize())
                .recordStats()
                .build();
    }

    private SQLClient createSQLClient(DBConf config) {
        VertxOptions opt = new VertxOptions();
        opt.setWorkerPoolSize(config.getWorkPoolSize());
        opt.setEventLoopPoolSize(config.getEventLoopPoolSize());

        FileSystemOptions fileOption = opt.getFileSystemOptions();
        if (fileOption == null) {
            fileOption = new FileSystemOptions();
        }
        fileOption.setFileCacheDir("~/temp/vertx-cache");

        Vertx vertx = Vertx.vertx(opt);
        JsonObject jsonObject = new JsonObject()
                .put("url", config.getJdbcUri())
                .put("driver_class", config.getDriverClass())
                .put("max_pool_size", config.getMaxPoolSize())
                .put("user", config.getUserName())
                .put("password", config.getPassword());
        return JDBCClient.createShared(vertx, jsonObject);
    }

    @Override
    public void close() throws Exception {
        if(sqlClient !=null) {
            sqlClient.close();
        }
        if(cache!=null){
            cache.invalidateAll();
        }

    }

    protected abstract String buildSQL(I input);

    protected abstract Collection<O> buildOutMessage(I input, Map<String, C> cachedRecord);

    protected abstract Pair<Boolean, Map<String, C>> getCachedRecord(I input);

    protected abstract void handleSuccess(I input, ResultFuture<O> output, ResultSet resultSet);

    protected abstract void handleFailure(I input, ResultFuture<O> output);


    protected void process(I input, ResultFuture<O> output, SQLConnection conn, String sql, int retryNum) {
        conn.query(sql, queryResult -> {
            if (queryResult.failed()) {
                LOGGER.error("Error executing SQL query: " + sql, queryResult.cause());
                if (retryNum > 0) {
                    process(input, output, conn, sql, retryNum - 1);
                } else {
                    handleFailure(input, output);
                    conn.close();
                }
            } else {
                ResultSet resultSet = queryResult.result();
                handleSuccess(input, output, resultSet);
                conn.close();
            }
        });
    }

    @Override
    public void asyncInvoke(I i, ResultFuture<O> resultFuture) throws Exception {
        Pair<Boolean, Map<String, C>> cachedRecordWithFlag = getCachedRecord(i);
        if (cachedRecordWithFlag.getLeft()) {
            Collection<O> result = buildOutMessage(i, cachedRecordWithFlag.getRight());
            resultFuture.complete(result);
            return;
        }
        String sql = buildSQL(i);
        if(sql==null || sql.isEmpty()) {
            handleFailure(i, resultFuture);
            return;
        }

        int retryNum = this.dbConf.getRetryNum();
        getSqlClient().getConnection(connResult -> {
            if (connResult.failed()) {
                LOGGER.error("Cannot get MySQL connection via Vertx JDBC client ", connResult.cause());
                handleFailure(i, resultFuture);
                return;
            }
            SQLConnection conn = connResult.result();
            process(i, resultFuture, conn, sql, retryNum);
        });
    }

    @Override
    public void timeout(I input, ResultFuture<O> resultFuture) throws Exception {
        handleFailure(input, resultFuture);
    }
}
