package com.amazonaws.common;

import java.time.Duration;

public class CacheConf extends DBConf {
    private Duration writeExpireDuration;
    private int maxSize;

    public Duration getWriteExpireDuration() {
        return writeExpireDuration;
    }

    public void setWriteExpireDuration(Duration writeExpireDuration) {
        this.writeExpireDuration = writeExpireDuration;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
