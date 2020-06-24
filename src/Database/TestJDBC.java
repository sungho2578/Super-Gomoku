package Database;

import java.sql.*;

/*
    JDBC程序的编写步骤：
    1.注册驱动
    2.连接数据库
        DriverManager, Connection
    3.增删改
        Statement, PreparedStatement
       查询
        Statement, PreparedStatement , Result Set
    4.断开连接：关闭各种对应的资源

 */

/*

public class TestJDBC {
    public static void main(String args[]) throws SQLException
    {
        //registerDriver and connect to the Database
        String url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
        //this url is used to locate 1.which computer 2.what kind of dbms 3.which database 4.which port
        Connection conn = DriverManager.getConnection(url , "root" , "123456");
        String sql = "INSERT INTO t_stu VALUES(2,'lin')";
        //Connection类似网络编程的Socket Statement类似和服务器进行读写的InputStream和OutputStream
        //准备一个statement，通过Statement对象把sql语句发送给服务器
        Statement st = conn.createStatement();
        //增删改都需要使用update
        int len = st.executeUpdate(sql);
        System.out.println(len);
        String sql1 = "select * from t_stu";
        Statement st1 = conn.createStatement();
        ResultSet rs = st1.executeQuery(sql1);
        while(rs.next())
        {
            int sid = rs.getInt("ID");
            String name = rs.getString("name");
            System
        }
        //释放资源
        st.close();
        conn.close();


        //operate on DataBase


    }
}


 */