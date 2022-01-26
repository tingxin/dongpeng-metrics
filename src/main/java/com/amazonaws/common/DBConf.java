package com.amazonaws.common;

public class DBConf {
    private String jdbcUri;
    private String driverClass;
    private Integer maxPoolSize = 20;
    private String userName;
    private String password;
    private Integer workPoolSize = 10;
    private Integer eventLoopPoolSize = 5;
    private Integer retryNum = 3;

    public Integer getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(Integer retryNum) {
        this.retryNum = retryNum;
    }

    public String getJdbcUri() {
        return jdbcUri;
    }

    public void setJdbcUri(String jdbcUri) {
        this.jdbcUri = jdbcUri;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getWorkPoolSize() {
        return workPoolSize;
    }

    public void setWorkPoolSize(Integer workPoolSize) {
        this.workPoolSize = workPoolSize;
    }

    public Integer getEventLoopPoolSize() {
        return eventLoopPoolSize;
    }

    public void setEventLoopPoolSize(Integer eventLoopPoolSize) {
        this.eventLoopPoolSize = eventLoopPoolSize;
    }
}
