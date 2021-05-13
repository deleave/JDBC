package com.yth.JDBC上.exer;

import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @ClassName ExerInsert
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/7 17:02
 * @Version 1.0
 **/
public class ExerInsert {


    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入用户名：");
        String name = scanner.next();
        System.out.println("请输入邮箱：");
        String email = scanner.next();
        System.out.println("请输入生日：");
        String birthday = scanner.next();
        String sql="insert into customers(name,email,birth) values(?,?,?)";
        int insertCount = update(sql, name, email, birthday);
        if (insertCount>0){
            System.out.println("添加成功！");
        }else{
            System.out.println("添加失败！");
        }
    }

    //通用增删改操作
    public int update(String sql,Object...args) {
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
            /*
             *ps.execute();
             *如果执行的是查询操作，有返回结果，则此方法返回true
             * 如果执行的是增删改操作，没有返回值，则此方法返回false
             * */
        return     ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }
}
