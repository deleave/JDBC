package com.yth.JDBC上.jdbc2.bean;

import java.util.Date;

/**
 * @ClassName Order
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/7 14:44
 * @Version 1.0
 **/
public class Order {
    private int orderId;
    private String orderName;
    private Date orderDate;

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderName='" + orderName + '\'' +
                ", order_date=" + orderDate +
                '}';
    }

    public void setOrder_id(int orderId) {
        this.orderId = orderId;
    }

    public void setOrder_name(String orderName) {
        this.orderName = orderName;
    }

    public void setOrder_date(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrder_id() {
        return orderId;
    }

    public String getOrder_name() {
        return orderName;
    }

    public Date getOrder_date() {
        return orderDate;
    }

    public Order() {
    }

    public Order(int orderId, String orderName, Date orderDate) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDate = orderDate;
    }
}
