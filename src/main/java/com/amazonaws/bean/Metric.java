package com.amazonaws.bean;

import java.util.Date;

public class Metric {
    private final String name;
    private final Double value;
    private final Date occur;

    public Metric(String name, Double value, Date occur) {
        this.name = name;
        this.value = value;
        this.occur = occur;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }

    public Date getOccur() {
        return occur;
    }
}
