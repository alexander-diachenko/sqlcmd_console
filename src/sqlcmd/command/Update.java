package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Update implements Command {

    private DatabaseManager manager;

    public Update(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        manager.update(command);
    }
}