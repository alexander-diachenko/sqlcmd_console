package sqlcmd.databasemanager;

import sqlcmd.view.View;

import java.sql.*;
import java.util.ArrayList;

public class JDBCDatabaseManager implements DatabaseManager {

    private Connection connection;
    private View view;

    public JDBCDatabaseManager(View view) {
        this.view = view;
    }

    @Override
    public ArrayList<String> getTableNames() throws SQLException {

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, "public", "%", new String[]{"TABLE"});

        ArrayList<String> tableNames = new ArrayList<>();
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(3));
        }
        resultSet.close();
        return tableNames;
    }

    @Override
    public void update(String tableName, String value, String primeryKey) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE public." + tableName + " SET " + value + " WHERE " + primeryKey);
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

    @Override
    public void create(String tableName, String value) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO public." + tableName + " VALUES(" + value + ")");
        stmt.close();
    }

    @Override
    public void clear(String tableName) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public." + tableName);
        stmt.close();
    }

    @Override
    public void delete(String tableName, String primaryKey) throws SQLException {

        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM public." + tableName + " WHERE " + primaryKey);
        stmt.close();
    }

    @Override
    public void connect(String database, String user, String password) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            view.write("Не могу найти драйвер (jar). Добавьте его в библитеку проекта.");
        }
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/" + database + "", "" + user + "",
                "" + password + "");
    }

    @Override
    public ArrayList<String> getTableData(String tableName) throws SQLException {

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM public." + tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();

        ArrayList<String> tableData = new ArrayList<>();
        tableData.add(String.valueOf(rsmd.getColumnCount()));
        for (int indexColumn = 1; indexColumn <= rsmd.getColumnCount(); indexColumn++) {
            tableData.add(resultSet.getMetaData().getColumnName(indexColumn));
        }

        while (resultSet.next()) {
            for (int indexData = 1; indexData <= rsmd.getColumnCount(); indexData++) {
                if(resultSet.getString(indexData) == null){
                    tableData.add("");
                }else {
                    tableData.add(resultSet.getString(indexData));
                }
            }
        }
        stmt.close();
        resultSet.close();
        return tableData;
    }
}
