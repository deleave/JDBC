package com.yth.JDBC下.jdbc1.transaction;

import com.yth.JDBC上.jdbc2.bean.User;
import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @ClassName TransactionTest
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/9 14:20
 * @Version 1.0
 **/

/*
* 1.什么是数据库事务
* 事务：一组操作逻辑单元，使数据从一种状态转换成另一种状态
*  2.数据一旦提交不可回滚
* 3.哪些操作会导致数据自动提交？
*  >DDL操作一旦执行，都会自动提交
*  >DML默认情况下，自动提交
*    >我们可以通过set atuocommit=false 的方式取消DML操作的自动提交
*  >关闭连接后会自动提交
* */
    public class TransactionTest {
    /*
    ****************未考虑事务处理的转账操作***********************
    * 针对数据表user_table来说
    * AA用户给BB用户转账100
    *
    * update user_table set balance=balance-100 where user='AA'
    * update user_table set balance=balance+100 where user='BB'
    * */
    @Test
    public void testUpdate(){
        String sql="update user_table set balance=balance-100 where user=?";
        update(sql,"AA");
        //模拟网络异常
        System.out.println(10/0);
        String sql2="update user_table set balance=balance+100 where user=?";
        update(sql2,"BB");
        System.out.println("转账成功！");
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
    //*****************考虑数据库事务的转账操作**********************
    @Test
    public void testUpdateWithTx()  {
        Connection conn=null;
        try {
            //1.获取连接
            conn = JDBCUtils.getConnection();
           //2.取消自动提交
            System.out.println(conn.getAutoCommit());
             conn.setAutoCommit(false);
             //3.执行事务
            String sql1 = "update user_table set balance=balance-100 where user=?";
            update(conn, sql1, "AA");
            //3.1事务执行时模拟网络异常
        System.out.println(10/0);//AA账户未变
            String sql2 = "update user_table set balance=balance+100 where user=?";
            update(conn, sql2, "BB");
            System.out.println("转账成功");
            //4.事务正常执行完成后提交数据
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
            //4.1 事务执行异常回滚数据
            try {
                conn.rollback();
                //catch回滚异常
            }catch (SQLException e1){
                e.printStackTrace();
            }
        }finally {
            //5.关闭资源
            JDBCUtils.closeResource(conn,null);

        }
    }
    //通用增删改操作(version2.0) 增加事务处理
    public void update(Connection conn,String sql,Object...args) {
        PreparedStatement ps=null;
        try {

            //sql中占位符得分个数与可变长参数的长度相同
            //1.预编译sql语句，返回PreparedStatement的实例
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//注意i+1
            }
            //3.执行
            ps.execute();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //恢复为自动提交数据
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //4.资源的关闭
            JDBCUtils.closeResource(null, ps);
        }
    }

//*******************************************************
    @Test
    public void testTransactionSelect() throws Exception {
        Connection conn=JDBCUtils.getConnection();
        //获取当前连接的隔离级别
//        System.out.println(conn.getTransactionIsolation());
        //设置数据库的隔离级别
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        conn.setAutoCommit(false);
        String sql="select user,password,balance from user_table where user=?";
        User user = getInstance(conn, User.class, sql, "cc");
        System.out.println(user);
    }
    @Test
    public void testTransactionUpdate() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String sql="update user_table set balance = ? where user=?";
        conn.setAutoCommit(false);

        update(conn,sql,"5000","cc");
        //15秒后回滚
        Thread.sleep(15000);

        System.out.println("修改结束");

    }
//  通用查询操作，用于返回数据表中的一条记录（version2.0 ）考虑事务处理
    public <T>T getInstance(Connection conn,Class<T> clazz,String sql,Object...args){

    PreparedStatement ps=null;
    ResultSet rs=null;
    try {

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
        JDBCUtils.closeResource(null,ps,rs);
    }
    return null;

}

    }
