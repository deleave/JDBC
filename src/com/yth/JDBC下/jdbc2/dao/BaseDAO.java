package com.yth.JDBC下.jdbc2.dao;

import com.yth.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseDAO
 * @Description 封装了针对于数据表的操作（考虑事务）
 * @Author deleave
 * @Date 2021/5/9 16:22
 * @Version 1.0
 **/
//不能实例化
public abstract class BaseDAO {

    //  通用查询操作，用于返回数据表中的一条记录（version2.0 ）考虑事务处理
    public <T>T getInstance(Connection conn, Class<T> clazz, String sql, Object...args){

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
    //  通用查询操作，用于返回数据表中的多条记录（version2.0 ）考虑事务处理
    public <T> List<T> getForList(Connection conn,Class<T> clazz, String sql, Object...args){
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
            //创建集合对象
            ArrayList<T> list=new ArrayList<T>();
            while (rs.next()) {
                //通过反射获取泛型对象
                T t = clazz.newInstance();
                //给t对象属性赋值
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
                list.add(t);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
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
            ps.executeQuery();

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
    //用于查询特殊值的通用方法
    public <E>E getValue(Connection conn,String sql,Object...args) {
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {

         ps = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++){
            ps.setObject(i+1,args[i]);
        }
         rs = ps.executeQuery();
        if (rs.next()){
           return (E) rs.getObject(1);
        }}catch (Exception e){
            e.printStackTrace();
        }finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
