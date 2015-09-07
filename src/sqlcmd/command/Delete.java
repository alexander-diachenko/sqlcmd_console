package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Delete implements Commands {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete");
    }

    @Override
    public void process(String command) {
        manager.delete(command);
    }
}
