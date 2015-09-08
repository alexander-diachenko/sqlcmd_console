package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Connect implements Command {

    private DatabaseManager manager;

    public Connect(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("connect");
    }

    @Override
    public void process(String command) {
        manager.connect(command);
    }
}