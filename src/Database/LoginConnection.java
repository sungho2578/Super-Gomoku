package Database;

import java.sql.*;

public class LoginConnection {
    public static boolean vertifyAccount(String account , String password) throws SQLException
    {
        Connection conn = DriverManager.getConnection(DatabaseConfiguration.DATABASE_URL , DatabaseConfiguration.USER , DatabaseConfiguration.PASSWORD);
        String sql = "select * from Players p where p.account =" + "'" + account +"'" + ";";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while(rs.next())
        {
            String pas = rs.getString("password");
            if(pas.equals(password))
            {
                st.close();
                rs.close();
                return true;
            }
            else
            {
                st.close();
                rs.close();
                return false;
            }
        }
        st.close();
        rs.close();
        return false;
    }

    public static String[] getInfo(String account) throws SQLException
    {
        Connection conn = DriverManager.getConnection(DatabaseConfiguration.DATABASE_URL , DatabaseConfiguration.USER , DatabaseConfiguration.PASSWORD);
        String sql = "select * from Players p where p.account =" + "'" + account +"'" + ";";
        String[] retString = new String[3];
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next())
        {
            String acc = rs.getString("account");
            String name = rs.getString("name");
            String points = String.valueOf(rs.getInt("credit"));
            retString[0] = acc;
            retString[1] = name;
            retString[2] = points;
        }
        return retString;

    }
}
