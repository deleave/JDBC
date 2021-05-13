package com.yth.JDBC上.jdbc4.preparedstatement;

import com.yth.JDBC上.jdbc2.bean.Order;
import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;

/**
 * @ClassName OrderForQuery
 * @Description 针对于Order表的通用查询操作
 * @Author deleave
 * @Date 2021/5/7 14:43
 * @Version 1.0
 **/
public class OrderForQuery {
/*
*@ClassName OrderForQuery
*@Description  通用的针对Order表的查询操作
*@Author deleave
*@Date 2021/5/7 15:03
*@Param
**/
    public Order QueryForOrder(String sql,Object...args)  {
/*
* 针对于表的字段名和类的属性名不同的情况
* 1.必须声明sql时，用类的属性名来命名字段的别名
* 2.使用ResultSetMetaData时，需要使用getColumnLabel（）来替换getColumnName（）
*    获取类的别名
* 说明，如果sql中没有给字段起别名，getColumnLabel（）仍然获取列名
*
*
* */


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
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名 getColumnName()
                    //获取列的别名 getColumnLabel() 无别名则取表名
                    String columnName = rsmd.getColumnLabel(i + 1);

                    //通过反射将对象指定名columnName的属性赋值为指定的值columnValue
                    Field field = Order.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(order, columnValue);//确定赋值对象为order
                }
                return order;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps,rs);
        }
        return null;
    }

    @Test
    public void testQueryForOrder(){
        String sql="select order_id orderId,order_name orderName,order_date orderDate from test.order where order_id=?";
        Order order=QueryForOrder(sql,1);
        System.out.println(order);
    }


    @Test
    public void testQuery1() {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {

            //1.建立连接
             conn = JDBCUtils.getConnection();
            //2.预编译sql语句
            String sql = "select order_id,order_name,order_date from test.order where order_id=?";
             ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, 2);
            //4。执行sql语句并返回结果集
             rs = ps.executeQuery();
            //5.获取元数据的信息
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            if (rs.next()) {
                int id = (int) rs.getObject(1);
                String name = (String) rs.getObject(2);
                Date date = (Date) rs.getObject(3);
                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //6.关闭资源
            JDBCUtils.closeResource(conn,ps,rs);
        }

    }

}
