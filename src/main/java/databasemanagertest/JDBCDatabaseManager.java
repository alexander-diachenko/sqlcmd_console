package databasemanagertest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void table(String tableName, String primaryKey, Map<String, Object> data) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE TABLE " + tableName +
                "(" + primaryKey + " INT  PRIMARY KEY NOT NULL" +
                    getColumns(data) + ")");
        stmt.close();
    }

    private String getColumns(Map<String, Object> data) {
        String result = "";
        for (Map.Entry<String, Object> pair : data.entrySet()) {
            result += ", " + pair.getKey() + " " + pair.getValue();
        }
        return result;
    }

    @Override
    public List<String> getTableNames() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, "public", "%", new String[]{"TABLE"});

        List<String> tableNames = new ArrayList<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(3));
        }
        resultSet.close();
        return tableNames;
    }

    @Override
    public List<String> getTableData(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName);
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
    public void create(String tableName, Map<String, Object> data) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO " + tableName + " (" + getKeys(data) + ")" +
                                                 " VALUES (" + getValues(data) + ")");
        stmt.close();
    }

    private String getValues(Map<String, Object> data) {
        String values = "";
        for (Map.Entry<String, Object> pair : data.entrySet()) {
            values += "'" + pair.getValue() + "', ";
        }
        return values.substring(0, values.length() - 2);
    }

    private String getKeys(Map<String, Object> data) {
        String keys = "";
        for (Map.Entry<String, Object> pair : data.entrySet()) {
            keys += pair.getKey() + ",";
        }
        return keys.substring(0, keys.length() - 1);
    }

    @Override
    public void update(String[] data) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE public." + data[0] +
                " SET " + data[3] + " = '" + data[4] +
                "' WHERE " + data[1] + " = '" + data[2] + "'");
        stmt.close();
    }

    @Override
    public void delete(String tableName, String key, String value) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public." + tableName + " WHERE " + key + " = '" + value + "'");
        stmt.close();
    }

    @Override
    public void clear(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public." + tableName);
        stmt.close();
    }

    @Override
    public void drop(String tableName) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE public." + tableName);
        stmt.close();
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }
}
