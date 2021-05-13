package com.yth.JDBC下.jdbc1.transaction;

import com.yth.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

/**
 * @ClassName ConnectionTest
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/9 14:16
 * @Version 1.0
 **/
public class ConnectionTest {
    @Test
    public void testGetConnection() throws Exception {
        Connection conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }
}
