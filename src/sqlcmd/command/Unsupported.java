package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Unsupported implements Command {

    private DatabaseManager manager;

    public Unsupported(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return true;
    }

    @Override
    public void process(String command) {
    manager.unsupported(command);
    }
}