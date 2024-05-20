package org.dacs.quackstagramdatabase.database;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    DatabaseConfig databaseConfig;

    @Autowired
    EntityManager entityManager;

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.username}")
    private String USER;

    @Test
    public void getConnectionText() throws SQLException {
        Connection connection = databaseConfig.getConnection();
        Assertions.assertNotNull(connection);

        DatabaseMetaData metaData = connection.getMetaData();

        String dbProductName    = metaData.getDatabaseProductName();
        String dbProductVersion = metaData.getDatabaseProductVersion();
        String userName         = metaData.getUserName();
        String url              = metaData.getURL();

        System.out.println("Database Product Name: " + dbProductName);
        System.out.println("Database Product Version: " + dbProductVersion);
        System.out.println("User Name: " + userName);
        System.out.println("URL: " + url);

        Assertions.assertEquals("H2", dbProductName);
        Assertions.assertEquals("2.2.224 (2023-09-17)", dbProductVersion);
        Assertions.assertEquals(this.USER, userName);
        Assertions.assertEquals(this.URL.split(";")[0], url);

    }

    @Test
    public void addRowTest() throws SQLException, IllegalAccessException {
        DatabaseTest test1 = new DatabaseTest("test2", 2);
        entityManager.persist(test1);
    }


}
