package org.dacs.quackstagramdatabase.database;

import org.dacs.quackstagramdatabase.database.annotations.Column;
import org.dacs.quackstagramdatabase.database.annotations.Entity;
import org.dacs.quackstagramdatabase.database.annotations.Id;
import org.dacs.quackstagramdatabase.database.annotations.Defaulted;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class EntityManager {
    private final Connection connection;

    public EntityManager(DatabaseConfig databaseConfig) throws SQLException, ClassNotFoundException {
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
            if (field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Defaulted.class)) {
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
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                preparedStatement.setObject(i+1, values.get(i));
            }

            int result = preparedStatement.executeUpdate();

            // Handle generated keys
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                // get the first generated key. There should be only 1 for our case
                while (generatedKeys.next()) {
                    // iterate through the fields of the entity class
                    for (Field field : clazz.getDeclaredFields()) {
                        // the field has to be @Id and @Incremented
                        // typically there is only one
                        if (field.isAnnotationPresent(Defaulted.class)) {
                            field.setAccessible(true);
                            if (Timestamp.class.isAssignableFrom(field.getType())) {
                                Long timestampLong = generatedKeys.getLong(1);
                                field.set(entity, new Timestamp(timestampLong));
                            } else {
                                field.set(entity, generatedKeys.getObject(1, field.getType()));
                            }
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

    public <T> T find(Class<T> clazz, List<Object> ids) throws SQLException, IllegalAccessException {
        // check if the class is supported;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // extract the table name
        String tableName = entityAnnotation.tableName();

        StringBuilder idColumnNames = new StringBuilder();
        List<Object> idValues = new ArrayList<>();

        for (int i = 0; i < clazz.getDeclaredFields().length; i++) {
            Field field = clazz.getDeclaredFields()[i];
            if (field.isAnnotationPresent(Id.class)) {
                Column column = field.getAnnotation(Column.class);
                idColumnNames.append(column.name()).append(" = ? AND ");
                idValues.add(ids.get(i));
            }
        }

        // if the id column cannot be found, throw exception
        if (idColumnNames.length() <= 0) {
            throw new RuntimeException("No id column found in class " + clazz.getName());
        }

        // prepare the SQL select statement
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnNames.substring(0, idColumnNames.length() - 5);

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            // set the IDs
            for (int i = 0; i < idValues.size(); i++) {
                preparedStatement.setObject(i+1, idValues.get(i));
            }

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

    public <T> void delete(Class<T> clazz, Object id) throws SQLException {
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
        String sql = "TRUNCATE TABLE " + tableName + ";";

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            int result = preparedStatement.executeUpdate();
            this.connection.commit();
        }
    }

    public <T> List<T> findAll(Class<T> clazz) throws SQLException, IllegalAccessException {
        // check if the class is supported;
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not an @Entity");
        }

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        // extract the table name
        String tableName = entityAnnotation.tableName();

        String sql = "SELECT * FROM " + tableName;

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> entitiesList = new LinkedList<>();

                while(resultSet.next()) {
                    T entity = clazz.getDeclaredConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            Object value = resultSet.getObject(column.name());
                            field.setAccessible(true);
                            field.set(entity, value);
                        }
                    }
                    entitiesList.add(entity);
                }
                return entitiesList;

            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public List<Object> dynamicQuery(String queryString, List<Object> parameters) throws SQLException {
        List<Object> results = new LinkedList<>();
        // count the number of placeholder
        int placeholderCount = queryString.split("\\?").length-1;
        if (placeholderCount != parameters.size()) {
            throw new RuntimeException("placeholder count " + placeholderCount + " does not match parameter count " + parameters.size());
        }

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(queryString)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    results.add(resultSet.getObject(1));
                }
            }
        }
        return results;
    }

}
