package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Drop implements Command {

    private DatabaseManager manager;

    public Drop(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
        manager.drop(command);
    }
}