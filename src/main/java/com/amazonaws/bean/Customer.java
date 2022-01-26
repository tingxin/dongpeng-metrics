package com.amazonaws.bean;

public class Customer implements java.io.Serializable{
    private String email;
    private String sex;

    public Customer(String email, String sex, String level) {
        this.email = email;
        this.sex = sex;
        this.level = level;
    }

    private String level;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
