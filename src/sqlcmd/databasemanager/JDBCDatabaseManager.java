package sqlcmd.databasemanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        String sql = "CREATE TABLE " + tableName + "(" + primaryKey + " INT  PRIMARY KEY NOT NULL";

        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            sql += ", " + pair.getKey() + " " + pair.getValue();
            it.remove(); // avoids a ConcurrentModificationException
        }
        sql += ")";

        stmt.executeUpdate(sql);
        stmt.close();
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
    public void create(String tableName, String value) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO public." + tableName + " VALUES(" + value + ")");
        stmt.close();
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
