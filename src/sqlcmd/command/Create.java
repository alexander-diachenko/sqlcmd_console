package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Create implements Commands {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        manager.create(command);
    }
}