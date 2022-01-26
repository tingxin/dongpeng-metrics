package com.amazonaws.bean;

public class Customer implements java.io.Serializable{
    private String email;
    private String sex;
    private Integer level;

    public Customer(String email, String sex, Integer level) {
        this.email = email;
        this.sex = sex;
        this.level = level;
    }



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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
