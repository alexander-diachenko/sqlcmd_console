package ua.com.juja.positiv.sqlcmd.databasemanager;

import java.sql.*;
import java.util.*;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class JDBCDatabaseManager implements DatabaseManager {

    public static final String JDBC_POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/";
    private Connection connection;

    @Override
    public void connect(String database, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                JDBC_POSTGRESQL_URL + database + "", "" + user + "",
                "" + password + "");
    }

    @Override
    public void table(String tableName, String keyName, Map<String, Object> columnParameters) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE " + tableName +
                "(" + keyName + " INT  PRIMARY KEY NOT NULL" +
                getParameters(columnParameters) + ")");
        stmt.close();
    }

    private String getParameters(Map<String, Object> columnParameters) {
        String result = "";
        for (Map.Entry<String, Object> pair : columnParameters.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }

    @Override
    public Set<String> getTableNames() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, "public", "%", new String[]{"TABLE"});

        Set<String> tableNames = new LinkedHashSet<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(3));
        }
        resultSet.close();
        return tableNames;
    }

    @Override
    public List<String> getTableData(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();

        List<String> tableData = new ArrayList<>();
        tableData.add(String.valueOf(rsmd.getColumnCount()));
        for (int indexColumn = 1; indexColumn <= rsmd.getColumnCount(); indexColumn++) {
            tableData.add(resultSet.getMetaData().getColumnName(indexColumn));
        }

        while (resultSet.next()) {
            for (int indexData = 1; indexData <= rsmd.getColumnCount(); indexData++) {
                if (resultSet.getString(indexData) == null) {
                    tableData.add("");
                } else {
                    tableData.add(resultSet.getString(indexData));
                }
            }
        }
        stmt.close();
        resultSet.close();
        return tableData;
    }

    @Override
    public void create(String tableName, Map<String, Object> columnData) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO " + tableName + " (" + getColumnNames(columnData) + ")" +
                " VALUES (" + getColumnValues(columnData) + ")");
        stmt.close();
    }

    private String getColumnNames(Map<String, Object> columnData) {
        String keys = "";
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            keys += pair.getKey() + ", ";
        }
        return keys.substring(0, keys.length() - 2);
    }

    private String getColumnValues(Map<String, Object> columnData) {
        String values = "";
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            values += "'" + pair.getValue() + "', ";
        }
        return values.substring(0, values.length() - 2);
    }

    @Override
    public void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) throws SQLException {
        Statement stmt = connection.createStatement();
        for (Map.Entry<String, Object> pair : columnData.entrySet()) {
            stmt.executeUpdate("UPDATE " + tableName +
                    " SET " + pair.getKey() + " = '" + pair.getValue() +
                    "' WHERE " + keyName + " = '" + keyValue + "'");
        }
        stmt.close();
    }

    @Override
    public void delete(String tableName, String keyName, String keyValue) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM " + tableName + " WHERE " + keyName + " = '" + keyValue + "'");
        stmt.close();
    }

    @Override
    public void clear(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM " + tableName);
        stmt.close();
    }

    @Override
    public void drop(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE " + tableName);
        stmt.close();
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}
