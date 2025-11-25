package com.example.demo.Config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");

            connection = DriverManager.getConnection("jdbc:h2:mem:userDB", "sa", "");
        } catch (Exception e) {
            
        }
        return connection;
    }
}
