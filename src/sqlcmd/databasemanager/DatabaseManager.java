package sqlcmd.databasemanager;

import sqlcmd.Console;

import java.sql.Connection;

/**
 * Created by POSITIV on 07.09.2015.
 */
public interface DatabaseManager {

    void help();

    void connect(String command);

    String list(String command);

    String getTableData(String command);

    void create(String command);

    void update(String command);

    void delete(String command);

    void clear(String command);

    void drop(String command);

    void exit();

    void unsupported(String command);
}
