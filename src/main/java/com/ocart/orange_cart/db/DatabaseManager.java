package com.ocart.orange_cart.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manages the database connection pool using HikariCP.
 * This ensures efficient and high-performance database access,
 * as recommended in your research document. A connection pool is
 * crucial for a responsive application.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/orangecart";
    private static final String USER = "YOUR_USER_NAME"; // <-- IMPORTANT: Change this
    private static final String PASSWORD = "YOUR_PASS_WORD"; // <-- IMPORTANT: Change this

    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseManager() {}

    /**
     * Gets a connection from the connection pool.
     * @return A database connection.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

