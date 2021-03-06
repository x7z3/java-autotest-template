package ru.bootdev.test.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBQuery {

    private final Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private Integer columnCount;
    private String lastQuery;

    public DBQuery(String url) throws SQLException {
        this.connection = DriverManager.getConnection(url);
    }

    public void query(String query) throws SQLException {
        lastQuery = query;
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSetMetaData = resultSet.getMetaData();
        columnCount = resultSetMetaData.getColumnCount();
    }

    public <T> List<T> getResult(Class<T> resultModel) throws SQLException {
        Field[] modelFields = resultModel.getDeclaredFields();
        Constructor<?> constructor = resultModel.getConstructors()[0];
        boolean ignoreUnknownColumns = resultModel.getAnnotation(SQLIgnoreUnknownColumns.class) != null;

        if (!ignoreUnknownColumns && modelFields.length != columnCount)
            throw new SQLException("The given data model fields count is not equal to the SQL query result columns count");

        List<T> resultList = new ArrayList<>();
        while (resultSet.next()) {
            try {
                T resultRow = (T) constructor.newInstance();
                for (Field modelField : modelFields) {
                    changeObjectFieldValue(resultRow, modelField, ignoreUnknownColumns);
                }
                resultList.add(resultRow);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }

        resultSet = statement.executeQuery(lastQuery);
        return resultList;
    }

    private void changeObjectFieldValue(Object modelObject, Field modelField, boolean ignoreUnknownColumns)
            throws IllegalAccessException, SQLException, InvocationTargetException, NoSuchMethodException {
        String sqlColumnName = modelField.getAnnotation(SQLColumn.class).value();
        Class<?> modelFieldType = modelField.getType();
        try {
            Method method = modelObject.getClass().getMethod("set" + capitalizeFirstLetter(modelField.getName()), modelFieldType);
            method.invoke(modelObject, modelFieldType.cast(getSqlFieldData(sqlColumnName, modelFieldType)));
        } catch (IllegalAccessException | SQLException | NoSuchMethodException | InvocationTargetException e) {
            if (!ignoreUnknownColumns) throw e;
        }
    }

    private String capitalizeFirstLetter(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    private Object getSqlFieldData(String columnName, Class<?> cellType) throws SQLException {
        if (cellType == Integer.class) {
            return resultSet.getInt(columnName);
        } else if (cellType == Long.class) {
            return resultSet.getLong(columnName);
        } else if (cellType == Float.class) {
            return resultSet.getFloat(columnName);
        } else if (cellType == Double.class) {
            return resultSet.getDouble(columnName);
        } else if (cellType == String.class) {
            return resultSet.getString(columnName);
        } else {
            return resultSet.getObject(columnName);
        }
    }

    public List<HashMap<String, String>> getResult() throws SQLException {
        List<HashMap<String, String>> resultList = new ArrayList<>();

        while (resultSet.next()) {
            HashMap<String, String> row = new HashMap<>();
            for (int i = 0; i < columnCount; i++) {
                row.put(resultSetMetaData.getColumnName(i + 1), resultSet.getString(i + 1));
            }
            resultList.add(row);
        }

        resultSet = statement.executeQuery(lastQuery);
        return resultList;
    }

    public void close() throws SQLException {
        connection.close();
        statement.close();
    }
}