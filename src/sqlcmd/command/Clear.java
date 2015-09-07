package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Clear implements Commands {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        manager.clear(command);
    }
}