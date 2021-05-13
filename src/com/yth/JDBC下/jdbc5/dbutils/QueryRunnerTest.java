package com.yth.JDBC下.jdbc5.dbutils;
/*
* commons-dbutils是Apache组织提供的一个开源JDBC工具类库，
* 它是对JDBC的简单封装，封装了数据库的增删改查操作
* */

import com.yth.JDBC上.jdbc2.bean.Customer;
import com.yth.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName QueryRunnerTest
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/9 23:43
 * @Version 1.0
 **/
public class QueryRunnerTest {
    //测试插入
    @Test
    public void testInsert() throws Exception {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql = "insert into test.customers(name,email,birth)values(?,?,?)";
            int insertCount = runner.update(conn, sql, "陈粒", "lili@gemail.com", "1990-07-26");
            System.out.println("成功添加了" + insertCount + "条数据");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,null);
        }
        }

//    测试查询
    /*
    * BeanHandler: 是ResultSetHandler接口的实现类，用于封装表中的一条记录
    *ctrl+ait+t 快速surround with
    * */
    @Test
    public void testQuery1()  {
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql="select id,name,email,birth from test.customers where id=?";
            BeanHandler<Customer> handler=new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 4);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }

    }
// 测试查询多条记录
    @Test
    public void testQuery2()  {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
             conn = JDBCUtils.getConnection();
            String sql="select id,name,email,birth from test.customers ";
            BeanListHandler<Customer> handler=new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler);
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

    //测试MapHandler：是ResultSetHandler接口的实现类，对应表中的一条记录
    // 将字段及相应字段的值作为map的key和value
    @Test
    public void testQuery3()  {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql="select id,name,email,birth from test.customers where id=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 5);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

    //测试MapListHandler：是ResultSetHandler接口的实现类，对应表中的多条记录
    // 将字段及相应字段的值作为map的key和value，将map添加到LIst中
    @Test
    public void testQuery4()  {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql="select id,name,email,birth from test.customers where id<?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> mapList = runner.query(conn, sql, handler, 5);
            mapList.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }


    }

/*
*ScalarHandler:用于查询特殊值：max，min，count，avery
* */
    @Test
    public void testQuery5()  {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();
            String sql="select avg(id) from test.customers";
            ScalarHandler handler = new ScalarHandler();
            Object avg =  runner.query(conn, sql, handler );
            System.out.println(avg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

    /*
    * 自定义ResultSetHandler的实现类
    * */
    @Test
    public void testQuery6()  {
        Connection conn=null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCUtils.getConnection();

            String sql="select id,name,email,birth from test.customers where id =?";
            ResultSetHandler<Customer> handler=new ResultSetHandler<Customer>(){

                @Override
                public Customer handle(ResultSet rs) throws SQLException {

                    if (rs.next()){
                        int id=rs.getInt("id");
                        String name=rs.getString("name");
                        String email = rs.getString("email");
                        Date birth = rs.getDate("birth");
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }
                    return null;
                }
            };
            Customer customer = runner.query(conn, sql, handler, "18");
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn,null);
        }
    }

}
