package com.cloud.sso.server.dao;

import com.cloud.sso.server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends BaseDao{
    //通过数据库的连接操作数据库，实现增删改查（使用Statement类）
    public User getUserByUsername(String username) {
        Connection conn;
        User user = null;
        try {
            conn = getConnection();
            String s = "select username, password from user where username=?";
            PreparedStatement pst = conn.prepareStatement(s);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            //4.处理数据库的返回结果(使用ResultSet类)
            while (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        User user = userDao.getUserByUsername("test");
        System.out.println(user.getPassword());
    }



}
