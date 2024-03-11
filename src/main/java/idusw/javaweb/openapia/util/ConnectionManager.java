package idusw.javaweb.openapia.util;

import java.sql.*;
public class ConnectionManager {
    private static ConnectionManager instance = new ConnectionManager();
    private ConnectionManager() {}
    public static ConnectionManager getInstance() { return instance; }

    public Connection getConnection() {
        Connection conn = null;

        String dbName = "db_a202012007";
        String jdbcUrl = "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
        String dbUser = "ua202012007";
        String dbPass = "cometrue";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver를 메모리에 적재
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Load Errors");
        }
        try {
            conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
        } catch (SQLException e) {
            System.out.println("Connection Errors");
        } finally {
            return conn;
        }
    }
}
