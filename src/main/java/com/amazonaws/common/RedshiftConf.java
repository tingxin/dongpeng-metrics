package com.amazonaws.common;

public class RedshiftConf extends DBConf {
    @Override
    public String getDriverClass() {
        if(super.getDriverClass() == null) {
            return com.amazon.redshift.jdbc.Driver.class.getCanonicalName();
        }
        return super.getDriverClass();
    }
}
