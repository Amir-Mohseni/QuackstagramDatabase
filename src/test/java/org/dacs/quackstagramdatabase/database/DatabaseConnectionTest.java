package org.dacs.quackstagramdatabase.database;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

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
    @Order(1)
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
    @Order(2)
    public void addRowTest() throws SQLException, IllegalAccessException {
        DatabaseTest test1 = new DatabaseTest(1,"test2", 1);
        entityManager.persist(test1);
    }

    @Test
    @Order(3)
    public void addIncrementedRowTest() throws SQLException, IllegalAccessException {
        DatabaseTest test1 = new DatabaseTest("test2", 2);
        entityManager.persist(test1);
    }

    @Test
    public void findEntityTest() throws SQLException, IllegalAccessException {
        DatabaseTest databaseTest = entityManager.find(DatabaseTest.class, List.of(new String[]{"1"}));
        System.out.println(databaseTest);
        Assertions.assertNotNull(databaseTest);
        Assertions.assertEquals(databaseTest.getData1(), "test2");
    }

    @Test
    @Order(4)
    public void clearTest() throws SQLException, IllegalAccessException {
        entityManager.deleteAll(DatabaseTest.class);
    }

}
