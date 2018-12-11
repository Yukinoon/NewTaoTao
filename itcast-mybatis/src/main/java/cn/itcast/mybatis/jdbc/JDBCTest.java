package cn.itcast.mybatis.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTest {

    public static void main(String[] args) {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 创建数据库连接
            String url = "jdbc:mysql://127.0.0.1:3306/mybatis_0505";
            String user = "root";
            String password = "123456";
            connection = DriverManager.getConnection(url, user, password);

            // 创建statmenet
            String sql = "SELECT * FROM tb_user WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数、执行sql，2个参数，第一个是参数的下标，从1开始，第二个参数数据查询条件数据
            preparedStatement.setLong(1, 1L);

            resultSet = preparedStatement.executeQuery();

            // 遍历结果集
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getLong("id"));
                System.out.println("userName: " + resultSet.getString("user_name"));
                System.out.println("password: " + resultSet.getString("password"));
                System.out.println("name: " + resultSet.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接（资源）
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if(null != preparedStatement){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if(null != connection){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
