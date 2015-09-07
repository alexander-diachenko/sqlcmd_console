package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;

public class Connect implements Commands {

    DatabaseManager manager = new JDBCDatabaseManager();

    @Override
    public boolean canProcess(String command) {
        return command.equals("connect");
    }

    @Override
    public void process(String command) {
        manager.connect(command);
    }
}