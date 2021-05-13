package com.yth.JDBC上.jdbc4.preparedstatement;

import com.yth.JDBC上.jdbc2.bean.Customer;
import com.yth.JDBC上.jdbc2.bean.Order;
import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName PreparedStatementQueryTest
 * @Description 使用PreparedStatement实现对于不同表的通用查询操作
 * @Author deleave
 * @Date 2021/5/7 15:52
 * @Version 1.0
 **/
public class PreparedStatementQueryTest {

    @Test
    public void testgetForList(){
        String sql="select id,name,email,birth from test.customers where id<?";
        List<Customer> list = getForList(Customer.class, sql, 5);
        list.forEach(System.out::println);

        String sql1="select order_id orderId,order_name orderName,order_date orderDate from test.order where order_id>? ";
        List<Order> list1 = getForList(Order.class, sql1, 1);
//  语义      list1.stream().forEach(order -> System.out.println(order));
        list1.forEach(System.out::println);
    }
    public <T>List<T> getForList(Class<T> clazz,String sql,Object...args){
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {

            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            //创建集合对象
            ArrayList<T> list=new ArrayList<T>();
            while (rs.next()) {
                //通过反射获取泛型对象
                T t = clazz.newInstance();
                //给t对象属性赋值
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名 getColumnName()
                    //获取列的别名 getColumnLabel() 无别名则取表名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);//确定赋值对象为order
                }
               list.add(t);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }


        @Test
    public void testGetInstance(){
        //查询customers表中的数据
        String sql="select id,name,email from test.customers where id=?";
        Customer customer = getInstance(Customer.class, sql, 19);
        System.out.println(customer);
        //查询order表中的数据
        String sql1="select order_id orderId,order_name orderName,order_date orderDate from test.order where order_id=? ";
        Order order = getInstance(Order.class, sql1, 4);
        System.out.println(order);
    }

    /*
    *@ClassName PreparedStatementQueryTest
    *@Description 使用PreparedStatement实现针对于不同表的通用查询操作，返回一条数据
    *@Author deleave
    *@Date 2021/5/7 16:13
    *@Param [clazz, sql, args]
    **/
    public <T>T getInstance(Class<T> clazz,String sql,Object...args){

        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {

            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                //通过反射获取泛型对象
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名 getColumnName()
                    //获取列的别名 getColumnLabel() 无别名则取表名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columnValue);//确定赋值对象为order
                }
                return t;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;

    }
}
