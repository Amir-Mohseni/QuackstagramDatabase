package org.dacs.quackstagramdatabase.database;

import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;
import org.dacs.quackstagramdatabase.database.annotations.Incremented;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class EntityManager {
    private final Connection connection;

    public EntityManager(DatabaseConfig databaseConfig) throws SQLException {
        this.connection = databaseConfig.getConnection();
        this.connection.setAutoCommit(false);
    }

    public <T> void persist(T entity) throws SQLException, IllegalAccessException {
        // check if the class is supported
        Class<?> clazz = entity.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        // Get the declared class-level annotation on the class
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // Get the table name declared through the annotation
        // This name will be the table name in the database
        String tableName = entityAnnotation.tableName();

        StringBuilder columnNames = new StringBuilder();
        StringBuilder columnValuesPlaceholder = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Incremented.class)) {
                // get the field-level annotation @Column
                Column column = field.getAnnotation(Column.class);

                // bypass java's access level checks
                field.setAccessible(true);


                // get the column name declared through the annotation
                // and append it to the columnNames
                columnNames.append(column.name()).append(",");
                // do the same for the values
                // these are for the SQL string
                columnValuesPlaceholder.append("?,");
                // then append the actual value to values
                values.add(field.get(entity));
            }
        }

        // craft the SQL insertion string
        String sql = "INSERT INTO " + tableName + " (" + columnNames.substring(0, columnNames.length() - 1) + ") VALUES (" + columnValuesPlaceholder.substring(0, columnValuesPlaceholder.length() - 1) + ")";

        // now insert the actual values into the SQL string
        // first convert the SQL string to a PreparedStatement
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i+1, values.get(i));
            }

            int result = preparedStatement.executeUpdate();

            // Handle generated keys
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                // get the first generated key. There should be only 1 for our case
                if (generatedKeys.next()) {
                    // iterate through the fields of the entity class
                    for (Field field : clazz.getDeclaredFields()) {
                        // the field has to be @Id and @Incremented
                        // typically there is only one
                        if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Incremented.class)) {
                            field.setAccessible(true);
                            field.set(entity, generatedKeys.getObject(1, field.getType()));
                        }
                    }
                }
            }

            if (result == 1) {
                this.connection.commit();
            } else {
                connection.rollback();
                throw new RuntimeException("Persist affected more or less than only one row. Rollback has been initiated.");

            }
        }
    }

    public <T> T find(Class<T> clazz, Object id) throws SQLException, IllegalAccessException {
        // check if the class is supported;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // extract the table name
        String tableName = entityAnnotation.tableName();

        String idColumn = null;
        //iterate though the fields to find the id column
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                idColumn = column.name();
                break;
            }
        }

        // if the id column cannot be found, throw exception
        if (idColumn == null) {
            throw new RuntimeException("No id column found in class " + clazz.getName());
        }

        // prepare the SQL select statement
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            // set the ID
            preparedStatement.setObject(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        // we will only work with columns annotated with @Class
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            Object value = resultSet.getObject(column.name());
                            field.setAccessible(true);
                            field.set(entity, value);
                        }
                    }
                    return entity;
                }
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public <T> void delete(Class<T> clazz, Object id) throws SQLException, IllegalAccessException {
        // check if the class is supported;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        // get the @Entity class-level annotation
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // extract the table name
        String tableName = entityAnnotation.tableName();

        // now search for the id column
        String idColumn = null;
        //iterate though the fields to find the id column
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                idColumn = column.name();
                break;
            }
        }

        // if the id column cannot be found, throw exception
        if (idColumn == null) {
            throw new RuntimeException("No id column found in class " + clazz.getName());
        }

        // prepare the SQL delete statement
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                this.connection.commit();
            } else {
                connection.rollback();
                throw new RuntimeException("Delete affected more or less than only one row. Rollback has been initiated.");
            }
        }

    }

    public <T> void deleteAll(Class<T> clazz) throws SQLException, IllegalAccessException {
        // check if the class is supported;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        // get the @Entity class-level annotation
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // extract the table name
        String tableName = entityAnnotation.tableName();

        String idColumn = null;
        //iterate though the fields to find the id column
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                idColumn = column.name();
                break;
            }
        }

        // prepare the SQL truncate statement
        String sql = "TRUNCATE TABLE " + tableName + "; ALTER TABLE " + tableName + " ALTER COLUMN " + idColumn + " RESTART WITH 1";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            int result = preparedStatement.executeUpdate();
            this.connection.commit();
        }
    }

}
