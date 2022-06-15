package com.example.practicerestfulwebservice.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {
    private static Connection connection;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            Class.forName(Config.DATABASE_DRIVER_CLASS);
            connection =
                    DriverManager.getConnection(Config.DATABASE_URL, Config.DATABASE_USER, Config.DATABASE_PASSWORD);
            System.out.println("Connect success!");
        } else {
            System.out.println("Use existing connection!");
        }
        return connection;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionHelper.getConnection();
    }
}
