package com.yth.JDBC上.jdbc4.preparedstatement;

import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;
/*
* 使用PreparedStatement来替换，实现对数据表得分增删改查操作
*PreparedStatement是Statement接口的子接口
*
* 增删改：操作后不需要有返回
* 查：
* */
public class PreparedStatementUpdateTest {


    //向customer表中添加一个记录
    @Test
    public void testInsert()  {
        //建立连接
        PreparedStatement ps=null;
        Connection connection=null;
        try {
//        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        properties.load(is);
        //1.获取4个基本信息
        String user=properties.getProperty("user");
        String password=properties.getProperty("password");
        String url=properties.getProperty("url");
        String driverClass=properties.getProperty("driverClass");
        //2.加载驱动
        Class.forName(driverClass);
        //3.获取连接
      connection = DriverManager.getConnection(url, user, password);

        //预编译sql语句，返回PreparedStatement的实例
        String sql="insert into test.customers(name,email,birth)values (?,?,?)";
      ps = connection.prepareStatement(sql);
        //填充占位符
        ps.setString(1,"孙燕姿");
        ps.setString(2,"yanzi@gmail.com");
        //传入时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = sdf.parse("1000-01-01");
        ps.setDate(3,new Date(date.getTime()));
        //6.执行操作
        ps.execute();}
        catch (Exception e){
            e.printStackTrace();
        }finally {
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
                if (connection!=null)
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    //修改customers表的记录
    @Test
    public void testUpdate()  {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            //1.获取数据库连接
         conn = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            String sql = "update test.customers set birth=? where id=?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setObject(1, "1978-07-23");
            ps.setObject(2, 19);
            //4.执行sql语句
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
        }
    }

    //通用增删改操作
    public void update(String sql,Object...args) {
        Connection connection=null;
        PreparedStatement ps=null;
        try {

            //sql中占位符得分个数与可变长参数的长度相同
            //1.获取数据库连接
            connection = JDBCUtils.getConnection();
            //2.预编译sql语句，返回PreparedStatement的实例
            ps = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//注意i+1
            }
            //4.执行
            ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
    }

    @Test
    public void testCommonDelete(){
//        String sql="delete from test.customers where id=?";
//        update(sql,3);

        String sql="update  test.order set order_name=? where order_id=?";
        update(sql,"MM","2");
    }

}
