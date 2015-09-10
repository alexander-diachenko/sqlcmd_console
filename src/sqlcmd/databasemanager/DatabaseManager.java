package sqlcmd.databasemanager;

public interface DatabaseManager {

    void help();

    void connect(String command);

    String getTableNames(String command);

    String getTableData(String command);

    void create(String command);

    void update(String command);

    void delete(String command);

    void clear(String command);

    void drop(String command);

    void exit();

    void unsupported(String command);
}
