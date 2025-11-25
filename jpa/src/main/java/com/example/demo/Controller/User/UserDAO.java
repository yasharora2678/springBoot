package com.example.demo.Controller.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.demo.Config.DBConnection;

public class UserDAO {
    public void createUserTable() {
        try {
            Connection connection = new DBConnection().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE users (user_id AUTO_INCREMENT PRIMARY KEY, user_name VARCHAR(100), age INT)");
        } catch (SQLException e) {
            throw new RuntimeException("Can't create user table", e);
        }
    }

    public void createUser(String username, int Age) {
        try {
            Connection connection = new DBConnection().getConnection();
            String sqlQuery = "INSERT INTO users(user_name , age) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, Age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't add user", e);
        }
    }

    public String getUsers() {
        String userDetails = null;
        try {
            Connection connection = new DBConnection().getConnection();
            String sqlQuery = "SELECT * from users";
            PreparedStatement preparedQuery = connection.prepareStatement(sqlQuery);
            ResultSet output = preparedQuery.executeQuery();
            while (output.next()) {
                userDetails = output.getInt("user_id") + ":" + output.getString("user_name") + ":"
                        + output.getInt("age");
                System.out.println(userDetails); 
            }
        } catch (SQLException e) {
            throw new RuntimeException("Can't get users details", e);
        }
        return userDetails;
    }
}
