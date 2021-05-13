package com.yth.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
*@ClassName JDBCUtils 
*@Description  操作数据库得分工具类
*@Author deleave
*@Date 2021/5/6 18:47
**/
public class JDBCUtils {
    /*
    *
    *@Description 获取数据库的连接
    *@Author deleave
    *@Date 2021/5/6 19:54
    *@Param
    **/
    public static Connection getConnection1() throws Exception{
        //1.读取配置文件的4个基本信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        String user=properties.getProperty("user");
        String password=properties.getProperty("password");
        String url=properties.getProperty("url");
        String driverClass=properties.getProperty("driverClass");

        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
       return connection;
    }
    /*
    *@ClassName JDBCUtils
    *@Description 使用c3p0的数据库连接池技术
    *@Author deleave
    *@Date 2021/5/9 20:27
    *@Param
    **/
    //只需提供一个数据库连接池
   private static ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
    public static Connection getConnection2() throws SQLException {

        Connection conn = cpds.getConnection();
        return conn;
    }

  private static   DataSource source;
    static   {
        try {
            Properties pros = new Properties();

            //方式一：类的加载器
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            //方式二：
            FileInputStream is =new FileInputStream(new File("src/dbcp.properties"));

            pros.load(is);
            //创建DBCP数据库连接池
            source = BasicDataSourceFactory.createDataSource(pros);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {

        Connection conn = source.getConnection();
        return conn;
    }

    /*
    *@ClassName JDBCUtils
    *@Description 利用Druid数据库连接池获取连接
    *@Author deleave
    *@Date 2021/5/9 23:33
    *@Param []
    **/
    private static DataSource source1;
    static {
        try {
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("Druid.properties");
            pros.load(is);
            source1 = DruidDataSourceFactory.createDataSource(pros);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws Exception {

        Connection conn = source1.getConnection();
        return conn;
    }
    /*
    *@ClassName JDBCUtils
    *@Description 关闭连接和statement的操作
    *@Author deleave
    *@Date 2021/5/6 19:56
    *@Param [conn, ps]
    **/
    public static void closeResource1(Connection conn, Statement ps,ResultSet rs){
        //7.资源关闭
        try {
            //避免空值调用
            if (ps !=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            //避免空值调用
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            if (rs!=null)
                rs.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void closeResource(Connection conn, Statement ps){
        //7.资源关闭
        try {
            //避免空值调用
            if (ps !=null)
                ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            //避免空值调用
            if (conn!=null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
    *@ClassName JDBCUtils
    *@Description 使用dbutils.jar中提供的DbUtils工具类，实现资源的关闭
    *@Author deleave
    *@Date 2021/5/10 10:55
    *@Param [conn, ps]
    **/
    public static void closeResource(Connection conn, Statement ps,ResultSet rs){
   /*     try {
            DbUtils.close(conn);
            DbUtils.close(ps);
            DbUtils.close(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
//      工具类处理了异常
        DbUtils.closeQuietly(conn,ps,rs);

    }
}
