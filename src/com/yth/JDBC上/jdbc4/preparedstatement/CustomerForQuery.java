package com.yth.JDBC上.jdbc4.preparedstatement;

import com.yth.JDBC上.jdbc2.bean.Customer;
import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @ClassName CustomerForQuery1
 * @Description 针对Customers表的查询操作
 * @Author deleave
 * @Date 2021/5/6 20:33
 * @Version 1.0
 **/
public class CustomerForQuery {
/*
    *@ClassName CustomerForQuery1
    *@Description  针对Customers表的通用的查询操作
    *@Author deleave
    *@Date 2021/5/6 21:11
    *@Param []
    **/
    @Test
    public void testQueryForCustomers(){
        String sql="select id,name,email,birth from test.customers where id=?";
        Customer customer = queryForCustomers(sql, 18);
        System.out.println(customer);
    }

    public Customer queryForCustomers(String sql,Object...args)  {
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet resultSet=null;
        try {
            //1.建立连接
             connection = JDBCUtils.getConnection();
            //2.预编译sql语句
             ps = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //4.执行并返回结果集
             resultSet = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = resultSet.getMetaData();
            //通过ResultSetMetaData获取结果集中得分列数
            int columnCount = rsmd.getColumnCount();
            //5.处理结果集
            if (resultSet.next()) {
                Customer customer = new Customer();
                //处理结果集一行数据的每一列
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //给customer对象指定的某个属性赋值为value,通过反射。
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(customer, columnValue);
                }
                return customer;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection,ps,resultSet);
        }
        return null;
    }

    @Test
    public void testQuery1() {
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet resultSet=null;
        try {

            //1.创建连接
            connection = JDBCUtils.getConnection();
            //2.预编译sql语句
            String sql = "select id,name,email,birth from test.customers where id=?";
            ps = connection.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1,"1");
            //4.执行并返回结果集
            resultSet = ps.executeQuery();
            //5.处理结果集
            if (resultSet.next()) {//判断结果集的下一条数据是否有数据，有数据为true且指针下移，
                //获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);
                //方式一：
//                System.out.println("id" + id + ",name = " + name + ", email =" + email + ",birth = " + birth);
                //方式二：
                Object[] data = new Object[]{id, name, email, birth};
                //方式三：封装成一个bean对象（推荐）
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭资源
            JDBCUtils.closeResource(connection, ps, resultSet);
        }
    }
}
