package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Unsupported implements Commands{

    DatabaseManager manager = new JDBCDatabaseManager();

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
    manager.unsupported(command);
    }
}