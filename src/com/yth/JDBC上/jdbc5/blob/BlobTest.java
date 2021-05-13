package com.yth.JDBC上.jdbc5.blob;

import com.yth.JDBC上.jdbc2.bean.Customer;
import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

/**
 * @ClassName BlobTest
 * @Description 测试使用PreparedStatement操作Blob类型的数据
 * @Author deleave
 * @Date 2021/5/7 19:43
 * @Version 1.0
 **/
public class BlobTest {
    //向数据表customers中插入Blob类型字段
    @Test
    public void testInsert() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        String sql="insert into test.customers( name, email, birth, photo) VALUES (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setObject(1,"鬼刀");
        ps.setObject(2,"gd@gemail.com");
        ps.setObject(3,"1985-08-16");
        FileInputStream is = new FileInputStream(new File("lib/girl.jpg"));
        ps.setBlob(4,is);
        ps.execute();
        JDBCUtils.closeResource(conn,ps);
    }
    //查询数据表中customers中Blob类型的字段
    @Test
    public void testQuery()  {
        InputStream is=null;
        FileOutputStream fos=null;
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {

         conn = JDBCUtils.getConnection();
        String sql="select id, name, email, birth, photo from test.customers where name=?";
         ps = conn.prepareStatement(sql);
        ps.setObject(1,"鬼刀");
         rs = ps.executeQuery();
        if (rs.next()){
//            方式一：
//            int id = rs.getInt(1);
//            String name=rs.getString(2);
//            String email = rs.getString(3);
//            Date birth = rs.getDate(4);
//            方式二：利用别名
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            Date birth = rs.getDate("birth");
            Customer customer = new Customer(id, name, email, birth);
            System.out.println(customer);
            //将Blob类型的字段下载下来，以文件的方式保存在本地
            Blob photo = rs.getBlob("photo");
             is=photo.getBinaryStream();
             fos = new FileOutputStream("郭顶.jpg");
            byte[] buffer=new byte[1024];
            int len;
            while ((len=is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }

        }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is!=null)
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos!=null)
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn,ps,rs);
        }

    }
}
