package com.yth.JDBC上.jdbc1.connection;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest{
    @Test
    public void testConnection1() throws SQLException {
        //连接方式一
        //获取Driver实现类对象(获取驱动)
        Driver driver=new com.mysql.jdbc.Driver();
        //jdbc:mysql:协议
        //localhost:ip地址
        //3306:默认mysql端口号
        //test：test数据库
        String url="jdbc:mysql://localhost:3306/test";
        //将用户名和密码封装到Properties中
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","4513762");
        //创建Connection对象（利用驱动调用连接方法）
       Connection conn= driver.connect(url,info);
        System.out.println(conn);
    }
    //方式二：对方式一的迭代
    @Test
    public void testConnection2() throws Exception{
        //1.获取Driver实现类对象（利用反射使得程序具有更好得分可移植性）
      Class clazz= Class.forName("com.mysql.jdbc.Driver");
        Driver driver= (Driver) clazz.newInstance();
        //2.提供要链接的数据库
        String url="jdbc:mysql://localhost:3306/test";
        //3.提供要连接的用户名和密码
        Properties info=new Properties();
        info.setProperty("user","root");
        info.setProperty("password","4513762");
        //4.获取连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }
    //方式三：使用DriverManger替换Driver
    @Test
    public void  testConnection3() throws Exception {
        //1.获取Driver实现类的对象
        Class clazz=Class.forName("com.mysql.jdbc.Driver");
        Driver driver= (Driver) clazz.newInstance();

        //2.提供另外三个连接的基本信息
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="4513762";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
    //方式四：可以只是加载驱动，不用显示的注册驱动
    @Test
    public void  testConnection4() throws Exception {

        //1.提供另外三个连接的基本信息
        String url="jdbc:mysql://localhost:3306/test";
        String user="root";
        String password="4513762";
        //2.获取Driver实现类的对象
        Class clazz=Class.forName("com.mysql.jdbc.Driver");
//        Driver driver= (Driver) clazz.newInstance();
//        //注册驱动 可以省略
//        DriverManager.registerDriver(driver);
        /*  因为Driver实现类中含有如下操作：
              static {
            try {
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            } catch (SQLException var1) {
                throw new RuntimeException("Can't register driver!");
            }
        }*/
        //获取连接
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
    }
    //方式五：将数据库连接的4个基本信息放在配置文件中，通过读取配置文件来获取连接
    /*
    * 优点：
    * 1.实现了数据与代码的分离，解耦
    * 2.如果需要修改配置信息，不需要重新打包
    * */
    @Test
    public void getConnection5() throws IOException, ClassNotFoundException, SQLException {
        //1.读取配置文件的4个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

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
        System.out.println(connection);
    }
}
