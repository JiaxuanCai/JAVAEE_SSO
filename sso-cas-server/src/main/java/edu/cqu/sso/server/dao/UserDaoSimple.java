package edu.cqu.sso.server.dao;

import java.sql.*;

public class UserDaoSimple {
    private static final String URL = "jdbc:mysql://localhost:3306/sso?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String NAME = "root";
    private static final String PASSWORD = "trz000821";


    public static boolean check(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("select * from user");
            while (rs.next()) {
                if (rs.getString("username").equals(username)
                        && rs.getString("password").equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
        System.out.println(check("test", "test1"));
    }
}