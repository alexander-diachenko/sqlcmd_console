package sqlcmd.databasemanager;

import java.sql.SQLException;
import java.util.List;

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

    void create(String tableName, String value) throws SQLException;

    void update(String tableName, String value, String primaryKey) throws SQLException;

    void delete(String tableName, String primaryKey) throws SQLException;

    void clear(String tableName) throws SQLException;

    void drop(String tableName) throws SQLException;

    boolean isConnected();
}
