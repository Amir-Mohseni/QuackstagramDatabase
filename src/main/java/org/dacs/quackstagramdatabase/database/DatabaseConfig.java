package org.dacs.quackstagramdatabase.database;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConfig {
    private Connection connection;

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USER;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    public Connection getConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return this.connection;
    }

    @PreDestroy
    public void onDestroy() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.commit();
            this.connection.close();
        }
    }
}