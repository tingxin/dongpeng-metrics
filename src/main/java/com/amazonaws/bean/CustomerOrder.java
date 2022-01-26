package com.amazonaws.bean;

import java.util.Date;

public class CustomerOrder implements java.io.Serializable{
    private String email;
    private String sex;
    private String level;
    private String city;
    private int orderId;
    private String status;
    private int goodCount;
    private float amount;
    private Date createTime;

    private static final String UNKNOWN = "unknown";

    public CustomerOrder() {
    }

    public CustomerOrder(Customer c, OrderEvent e) {
        this.email = c.getEmail();
        this.sex =c.getSex();
        this.city = e.getCity();
        this.orderId = e.getOrderId();
        this.status = e.getStatus();
        this.goodCount = e.getGoodCount();
        this.amount = e.getAmount();
        this.createTime = e.getCreateTime();
    }

    public CustomerOrder(OrderEvent e) {
        this.email =e.getUserMail();
        this.orderId = e.getOrderId();
        this.status = e.getStatus();
        this.goodCount = e.getGoodCount();
        this.amount = e.getAmount();
        this.createTime = e.getCreateTime();

        this.setSex(UNKNOWN);
        this.setCity(UNKNOWN);
        this.setLevel(UNKNOWN);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
