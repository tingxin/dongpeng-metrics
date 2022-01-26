package com.amazonaws.common;

public class MysqlConf  extends  DBConf{
    @Override
    public String getDriverClass() {
        if(super.getDriverClass() == null) {
            return com.mysql.jdbc.Driver.class.getCanonicalName();
        }
        return super.getDriverClass();
    }
}
