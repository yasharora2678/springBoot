package com.example.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataBaseInitializer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void createTable() {
        jdbcTemplate
                .execute("CREATE TABLE users (user_id INT PRIMARY KEY AUTO_INCREMENT, user_name VARCHAR(100), age INT)");
    }
}
