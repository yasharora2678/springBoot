package com.example.demo.Repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Controller.User.User;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void createTable() {
        jdbcTemplate
                .execute("CREATE TABLE users (user_id AUTO_INCREMENT PRIMARY KEY, user_name VARCHAR(100), age INT)");
    }

    public void createUser(String name, int age) {
        String insertQuery = "INSERT INTO users(user_name , age) VALUES(?, ?)";
        jdbcTemplate.update(insertQuery, name, age);
    }

    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setName(rs.getString("user_name"));
            user.setAge(rs.getInt("age"));
            return user;
        });
    }
}
