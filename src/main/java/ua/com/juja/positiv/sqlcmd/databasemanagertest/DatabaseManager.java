package ua.com.juja.positiv.sqlcmd.databasemanagertest;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by POSITIV on 16.09.2015.
 */
public interface DatabaseManager {

    void connect(String database, String user, String command) throws SQLException, ClassNotFoundException;

    List<String> getTableNames() throws SQLException;

    /**
     * @param tableName .
     * @return tableData .
     * List[0] = columnCount.
     * List[1, columnCount + 1] = columnName.
     * List[columnCount + 1, size] = columnData.
     * @throws SQLException .
     */
    List<String> getTableData(String tableName) throws SQLException;

    void table(String tableName, String primaryKey, Map<String, Object> data) throws SQLException;

    void create(String tableName, Map<String, Object> data) throws SQLException;

    void update(String[] data) throws SQLException;

    void delete(String tableName, String key, String value) throws SQLException;

    void clear(String tableName) throws SQLException;

    void drop(String tableName) throws SQLException;

    boolean isConnected();
}
