package com.amazonaws.common;

public class RedshiftConf extends DBConf {
    @Override
    public String getDriverClass() {
       return "com.amazon.redshift.jdbc.Driver";
    }
}
