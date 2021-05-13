package com.yth.JDBC上.jdbc4.preparedstatement;

import com.yth.JDBC上.jdbc3.statement.User;
import com.yth.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @ClassName PreparedStatementTest
 * @Description 利用PreparedStatement替换Statement解决sql注入问题
 * @Author deleave
 * @Date 2021/5/7 16:31
 * @Version 1.0
 *
 * PreparedStatement解决sql注入问题的原理：预编译
 * 预编译将sql的逻辑确定，不会因为占位符的填充改变sql语句的逻辑
 *
 * 除了解决Statement的拼串，sql注入问题，PreparedStatement还有哪些好处？
 * 1.PreparedStatement可以操作Blob的数据，Statement做不到
 * 2.PreparedStatement可以实现高效的批量操作
 *
 **/
public class PreparedStatementTest {


    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("用户名：");
        String userName = scan.nextLine();
        System.out.print("密  码：");
        String password = scan.nextLine();

        // SELECT user,password FROM user_table WHERE USER = '1' or ' AND PASSWORD = '
        // ='1' or '1' = '1';
        String sql = "SELECT user,password FROM user_table WHERE USER = ? AND PASSWORD = ?";
        User user = getInstance(User.class,sql,userName,password);
        if (user != null) {
            System.out.println("登陆成功!");
        } else {
            System.out.println("用户名或密码错误！");
        }
    }
    public static  <T>T  getInstance(Class<T> clazz,String sql,Object...args){

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
