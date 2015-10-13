package ua.com.juja.positiv.sqlcmd.databasemanager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by POSITIV on 16.09.2015.
 */
public interface DatabaseManager {

    void connect(String database, String user, String command) throws SQLException, ClassNotFoundException;

    Set<String> getTableNames() throws SQLException;

    /**
     * @param tableName .
     * @return tableData .
     * List[0] = columnCount.
     * List[1, columnCount + 1] = columnName.
     * List[columnCount + 1, size] = columnData.
     * @throws SQLException .
     */
    List<String> getTableData(String tableName) throws SQLException;

    void table(String tableName, String keyName, Map<String, Object> columnParameters) throws SQLException;

    void create(String tableName, Map<String, Object> columnData) throws SQLException;

    void update(String tableName, String keyName, String keyValue, Map<String, Object> columnData) throws SQLException;

    void delete(String tableName, String keyName, String keyValue) throws SQLException;

    void clear(String tableName) throws SQLException;

    void drop(String tableName) throws SQLException;

    void createBase(String database) throws SQLException;

    void dropBase(String database) throws SQLException;

    boolean isConnected();
}
