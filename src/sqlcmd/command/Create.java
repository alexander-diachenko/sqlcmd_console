package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Create implements Command {

    private DatabaseManager manager;

    public Create(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        manager.create(command);
    }
}