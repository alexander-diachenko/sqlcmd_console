package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class List implements Command {

    private DatabaseManager manager;

    public List(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        manager.list(command);
    }
}

