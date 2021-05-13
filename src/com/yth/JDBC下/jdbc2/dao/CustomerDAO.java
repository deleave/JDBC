package com.yth.JDBC下.jdbc2.dao;

import com.yth.JDBC上.jdbc2.bean.Customer;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

/*
* 此接口用于规范针对customers表的常用操作
* */
public interface CustomerDAO {
    /*
    *@ClassName CustomerDAO
    *@Description 将cust对象添加到数据库中
    *@Author deleave
    *@Date 2021/5/9 16:37
    *@Param [conn, cust]
    **/
    void insert(Connection conn, Customer cust);


    /*
    *@ClassName CustomerDAO
    *@Description 根据指定的id，删除表的一条记录
    *@Author deleave
    *@Date 2021/5/9 16:38
    *@Param [conn, id]
    **/
    void deleteById(Connection conn, int id);


    /*
    *@ClassName CustomerDAO 
    *@Description 针对内存中的cust对象，去修改数据表中的指定记录
    *@Author deleave
    *@Date 2021/5/9 16:40
    *@Param [conn, cust]
    **/
    void update(Connection conn,Customer cust);


    /*
    *@ClassName CustomerDAO
    *@Description 针对指定的id拆线呢得到对应Customer对象
    *@Author deleave
    *@Date 2021/5/9 16:41
    *@Param [conn,id]
    **/
    Customer getCustomerById(Connection conn,int id);

    /*
    *@ClassName CustomerDAO
    *@Description 查询表中的所有记录构成的集合
    *@Author deleave
    *@Date 2021/5/9 16:43
    *@Param [conn]
    **/
    List<Customer> getAll(Connection conn);

    /*
    *@ClassName CustomerDAO
    *@Description 查询数据表中的数据的条目数
    *@Author deleave
    *@Date 2021/5/9 16:44
    *@Param [conn]
    **/
    Long getCount(Connection conn);

    /*
    *@ClassName CustomerDAO
    *@Description 返回数据表中的最大生日
    *@Author deleave
    *@Date 2021/5/9 16:45
    *@Param [conn]
    **/
    Date getMaxBirth(Connection conn);
}
