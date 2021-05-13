package com.yth.JDBC上.jdbc5.blob;

import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @ClassName InsertTest
 * @Description 使用PreparedStatement实现批量操作
 * @Author deleave
 * @Date 2021/5/7 20:40
 * @Version 1.0
 **/
/*
* updat，delete本身就具有批量操作特性
* 此时批量操作主要指批量插入，使用PreparedStatement实现批量操作
*
* 向goods表中插入20000条数据
* create table goods(
    id int primary key auto_increment,
    name varchar(26)
);
* 方式一：Statement
*Connection conn=JDBCUtils.getConnection();
* Statement st=conn.creatStatement();
* for(int i=0li<20000;i++){
* String sql="insert into goods(name)values('name_'+i+"'")";
* st.execute(sql);
*}
*  */
public class InsertTest {
    //批量插入的方式二,使用PreparedStatement
    @Test
    public void testInsert1()  {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            long start=System.currentTimeMillis();
             conn = JDBCUtils.getConnection();
            String sql = "insert into test.goods ( name) VALUES (?)";
             ps = conn.prepareStatement(sql);
            for (int i = 1; i < 20000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end=System.currentTimeMillis();
            System.out.println("花费时间为："+(end-start));//28117毫秒
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
    //方式三：
    /*
    * 1.addBatch()  executeBatch()  clearBatch()
    * 2.mysql服务器默认是关闭批处理的，需要一个参数，让mysql开启批处理
    * ?rewriteBatchedStatements=true,卸载配置文件url后面
    * 3.使用更新的mysql 驱动： mysql-connector-java-5.1.37-bin.jar
    * */
    @Test
    public void testInsert2()  {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            long start=System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into test.goods ( name) VALUES (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i < 2000000; i++) {
                ps.setObject(1, "name_" + i);
              ps.addBatch();
               //1.攒sql
                if (i%500==0){
                    //执行sql
                    ps.executeBatch();
                    //清空sql
                    ps.clearBatch();
                }
            }
            long end=System.currentTimeMillis();
            System.out.println("花费时间为："+(end-start));//2000000：18880ms
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
//方式三：利用事务管理的思想，先缓存完成后再统一提交sql
    @Test
    public void testInsert3()  {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            long start=System.currentTimeMillis();

            conn = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);
            String sql = "insert into test.goods ( name) VALUES (?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i < 2000000; i++) {
                ps.setObject(1, "name_" + i);
                ps.addBatch();
                //1.攒sql
                if (i%500==0){
                    //执行sql
                    ps.executeBatch();
                    //清空sql
                    ps.clearBatch();
                }
            }
            //缓存完成，统一提交
            conn.commit();
            long end=System.currentTimeMillis();
            System.out.println("花费时间为："+(end-start));//2000000：12410ms
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(conn,ps);
        }

    }
}
