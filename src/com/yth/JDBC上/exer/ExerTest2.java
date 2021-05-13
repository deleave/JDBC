package com.yth.JDBC上.exer;

import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

/**
 * @ClassName ExerTest2
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/7 17:30
 * @Version 1.0
 **/
public class ExerTest2 {
    //问题1：向examstudent表中添加一条数据
    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("四/六级：");
        int type = scanner.nextInt();
        System.out.println("请输入身份证号：");
        String IDCard = scanner.next();
        System.out.println("请输入学号：");
        String ExamCard = scanner.next();
        System.out.println("请输入学生姓名：");
        String StudentName = scanner.next();
        System.out.println("请输入生源地：");
        String Location = scanner.next();
        System.out.println("请输入成绩：");
        int Grade = scanner.nextInt();
        String sql="insert into test.examstudent(type,IDCard,ExamCard,StudentName,Location,Grade)values(?,?,?,?,?,?) ";
        int updateCount = update(sql, type, IDCard, ExamCard, StudentName, Location, Grade);
        if (updateCount>0){
            System.out.println("添加成功！");
        }else {
            System.out.println("添加失败！");
        }

    }

    //
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
            return ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //5.资源的关闭
            JDBCUtils.closeResource(connection, ps);
        }
        return 0;
    }
    //问题2：根据身份证号或者准考证号查询学生成绩信息
    @Test
    public void queryWithIDCardOrExamCard(){
        System.out.println("请输入你要检索的类型：");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String select = scanner.next();
        //防止空指针异常 忽略大小写
        if ("a".equalsIgnoreCase(select)){
            System.out.println("请输入准考证号：");
            String exeamCard = scanner.next();
            String sql="select FlowID ,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade  from test.examstudent where examCard=?";
            Student student = getInstance(Student.class, sql, exeamCard);
           if (student!=null){

               System.out.println(student);
           }else {
               System.out.println("输入的准考证号有误");
           }
        }else if ("b".equalsIgnoreCase(select)){
            System.out.println("请输入身份证号：");
            String IDCard = scanner.next();
            String sql="select FlowID ,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade  from test.examstudent where IDCard=?";
            Student student = getInstance(Student.class, sql, IDCard);
            if (student!=null){

                System.out.println(student);
            }else {
                System.out.println("输入的身份证号有误");
            }
        }else{
            System.out.println("您的输入有误，请重新进入程序。");
        }
    }

    public <T>T getInstance(Class<T> clazz,String sql,Object...args){

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
    //问题3：删除指定的学生信息
    @Test
    public void testDeleteByExamCard(){
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        //先查询指定准考证号的学生
        String sql="select FlowID ,Type type,IDCard,ExamCard examCard,StudentName name,Location location,Grade grade  from test.examstudent where examCard=?";
        Student student = getInstance(Student.class, sql, examCard);
        if (student==null){
            System.out.println("查无此人，请重新输入");
        }else {
            String sql1="delete from test.examstudent where ExamCard=?";
            int deleteCount = update(sql1, examCard);
            if (deleteCount>0){
                System.out.println("删除成功！");
            }else {
                System.out.println("删除失败！");
            }
        }
    }
    //优化:不必先查再删
    @Test
    public void testDeleteByExamCard1(){
        System.out.println("请输入学生的考号：");
        Scanner scanner = new Scanner(System.in);
        String examCard = scanner.next();
        String sql1="delete from test.examstudent where ExamCard=?";
        int deleteCount = update(sql1, examCard);
        if (deleteCount>0){
            System.out.println("删除成功！");
        }else {
            System.out.println("查无此人，请重新输入");
        }
    }
}
