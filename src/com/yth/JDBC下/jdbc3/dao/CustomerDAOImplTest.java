package com.yth.JDBC下.jdbc3.dao;

import com.yth.JDBC上.jdbc2.bean.Customer;
import com.yth.JDBC下.jdbc2.dao.CustomerDAOImpl;
import com.yth.util.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

class CustomerDAOImplTest {

  private com.yth.JDBC下.jdbc2.dao.CustomerDAOImpl dao=  new CustomerDAOImpl();
    @org.junit.jupiter.api.Test
    void insert() {
        Connection conn=null;
        try {

             conn = JDBCUtils.getConnection();
            Customer cust = new Customer(15, "薛之谦", "qzhi@qq.com", new Date(186889877));
            dao.insert(conn, cust);
            System.out.println("添加成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void deleteById() {
        Connection conn=null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn,12);
            System.out.println("删除成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void update() {
        Connection conn=null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer=new Customer(23,"薛之谦","qzhi@126.com",new Date(213123113));
            dao.update(conn,customer);
            System.out.println("修改成功");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void getCustomerById() {
        Connection conn=null;
        try {

            conn = JDBCUtils.getConnection2();

            Customer customer = dao.getCustomerById(conn, 21);
            System.out.println(customer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void getAll() {
        Connection conn=null;
        try {

            conn = JDBCUtils.getConnection();
            List<Customer> customers = dao.getAll(conn);
            System.out.println(customers);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void getCount() {
        Connection conn=null;
        try {

            conn = JDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println(count);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    @org.junit.jupiter.api.Test
    void getMaxBirth() {
        Connection conn=null;
        try {

            conn = JDBCUtils.getConnection();
            java.util.Date maxBirth = dao.getMaxBirth(conn);
            System.out.println(maxBirth);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
    }
}