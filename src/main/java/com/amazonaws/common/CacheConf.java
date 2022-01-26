package com.amazonaws.common;

import java.time.Duration;

public class CacheConf extends DBConf {
    private Duration writeExpireDuration;
    private int maxSize;

    public Duration getWriteExpireDuration() {
        return writeExpireDuration;
    }

    public CacheConf(Duration writeExpireDuration, int maxSize) {
        this.writeExpireDuration = writeExpireDuration;
        this.maxSize = maxSize;
    }
    public int getMaxSize() {
        return maxSize;
    }
}
