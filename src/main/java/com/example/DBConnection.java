package com.example;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        // 使用类加载器获取资源目录中的数据库文件
        String url = "jdbc:sqlite::resource:CSCI7785_database.db";
        return DriverManager.getConnection(url);
    }
}
