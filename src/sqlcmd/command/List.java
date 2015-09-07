package sqlcmd.command;


import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.databasemanager.JDBCDatabaseManager;


public class List implements Commands {

    DatabaseManager manager = new JDBCDatabaseManager();


    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        manager.list(command);
    }
}

