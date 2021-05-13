package com.yth.JDBC下.jdbc4.connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName DBCPTest
 * @Description 测试DBCP的数据库连接池技术
 * @Author deleave
 * @Date 2021/5/9 20:32
 * @Version 1.0
 **/
public class DBCPTest {
    /*
    *@ClassName DBCPTest
    *@Description 测试DBCP的数据库连接技术
    *@Author deleave
    *@Date 2021/5/9 22:20
    *@Param []
    **/
    @Test
    public void testGetConnection() throws SQLException {

        //创建DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("4513762");
        //可以设置其他设计数据库连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //建立连接
        Connection conn = source.getConnection();
        System.out.println(conn);
    }
    //方式二：使用配置文件
    @Test
    public void testGetConnection2() throws Exception {
        Properties pros = new Properties();
        //方式一：类的加载器
//        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
       //方式二：
        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        pros.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(pros);
        Connection conn = source.getConnection();
        System.out.println(conn);

    }
}
