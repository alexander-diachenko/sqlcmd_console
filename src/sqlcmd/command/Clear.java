package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Clear implements Command {

    private DatabaseManager manager;

    public Clear(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("clear|");
    }

    @Override
    public void process(String command) {
        manager.clear(command);
    }
}