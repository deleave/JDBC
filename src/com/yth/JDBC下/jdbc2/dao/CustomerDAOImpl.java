package com.yth.JDBC下.jdbc2.dao;

import com.yth.JDBC上.jdbc2.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/**
 * @ClassName CustomerＤＡＯＩｍｐｌ
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/9 16:47
 * @Version 1.0
 **/
public class CustomerDAOImpl extends BaseDAO implements CustomerDAO  {
    @Override
    public void insert(Connection conn, Customer cust) {
        String sql="insert into customers(name,email,birth)values (?,?,?)";
        update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql="delete from customers where id=?";
        update(conn,sql,id);
    }

    @Override
    public void update(Connection conn, Customer cust) {
        String sql="update customers set name=?,email =?,birth = ? where id=?";
        update(conn,sql,cust.getName(),cust.getEmail(),cust.getBirth(),cust.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql="select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(conn, Customer.class, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        String sql="select id,name,email,birth from customers";
        List<Customer> customers =  getForList(conn, Customer.class, sql);
        return customers;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql="select Count(*) from customers";
        return    getValue(conn,sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql="select max(birth) from customers";
       return getValue(conn,sql);
    }
}
