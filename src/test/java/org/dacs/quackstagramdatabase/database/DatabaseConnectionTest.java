package org.dacs.quackstagramdatabase.database;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConnectionTest {

    private DatabaseConfig databaseConfig;

    private EntityManager entityManager;

    private String URL = "jdbc:mysql://10.8.0.4:3306/Quackstagram?useSSL=true";

    private String USER = "Amir";

    private String PASSWORD = "Amir";

    @BeforeEach
    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        databaseConfig = new DatabaseConfig();
        databaseConfig.setConnection(DriverManager.getConnection(URL, USER, PASSWORD));
        entityManager = new EntityManager(databaseConfig);
    }

    @Test
    public void getConnectionTest() throws SQLException {
        Connection connection = databaseConfig.getConnection();
        Assertions.assertNotNull(connection);
        DatabaseMetaData metaData = connection.getMetaData();
        String dbProductName = metaData.getDatabaseProductName();
        String dbProductVersion = metaData.getDatabaseProductVersion();
        String userName = metaData.getUserName();
        String url = metaData.getURL();
        System.out.println("Database Product Name: " + dbProductName);
        System.out.println("Database Product Version: " + dbProductVersion);
        System.out.println("User Name: " + userName);
        System.out.println("URL: " + url);
        Assertions.assertEquals("MySQL", dbProductName);
        Assertions.assertEquals("8.0.36-2ubuntu3", dbProductVersion);
        Assertions.assertEquals("Amir@10.8.0.2", userName);
        Assertions.assertEquals("jdbc:mysql://10.8.0.4:3306/Quackstagram?useSSL=true".split(";")[0], url);
    }

    @Test
    public void addRowTest() throws SQLException, IllegalAccessException {
        DatabaseTestEntity test1 = new DatabaseTestEntity(1, "test2", 1);
        entityManager.persist(test1);
    }

    @Test
    public void addIncrementedRowTest() throws SQLException, IllegalAccessException {
        DatabaseTestEntity test1 = new DatabaseTestEntity("test2", 2);
        entityManager.persist(test1);
    }

    @Test
    public void findEntityTest() throws SQLException, IllegalAccessException {
        DatabaseTestEntity databaseTest = entityManager.find(DatabaseTestEntity.class, List.of(new String[]{"1"}));
        System.out.println(databaseTest);
        Assertions.assertNotNull(databaseTest);
        Assertions.assertEquals(databaseTest.getData1(), "test2");
    }

    @Test
    public void clearTest() throws SQLException, IllegalAccessException {
        entityManager.deleteAll(DatabaseTestEntity.class);
    }

    @Test
    public void moreThanXFollowersQueryTest() throws SQLException {
        List<Object> results = entityManager.dynamicQuery(QueryConfig.moreThanXFollowersQuery, Arrays.asList(new Object[]{-1}));
        Assertions.assertFalse(results.isEmpty());
    }
}
