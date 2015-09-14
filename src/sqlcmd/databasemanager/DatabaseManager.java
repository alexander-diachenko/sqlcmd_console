package sqlcmd.databasemanager;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DatabaseManager {

    void connect(String database, String user, String command) throws SQLException;

    ArrayList<String> getTableNames() throws SQLException;

    /**
     * @param tableName .
     * @return tableData .
     * ArrayList[0] = columnCount.
     * ArrayList[1, columnCount] = columnName.
     * ArrayList[columnCount + 1, size] = columnData.
     * @throws SQLException .
     */
    ArrayList<String> getTableData(String tableName) throws SQLException;

    void create(String tableName, String value) throws SQLException;

    void update(String tableName, String value, String primaryKey) throws SQLException;

    void delete(String tableName, String primaryKey) throws SQLException;

    void clear(String tableName) throws SQLException;

    void drop(String tableName) throws SQLException;

    boolean isConnected();
}
