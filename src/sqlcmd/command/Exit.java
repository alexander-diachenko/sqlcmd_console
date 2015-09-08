package sqlcmd.command;

import sqlcmd.Console;
import sqlcmd.databasemanager.DatabaseManager;

public class Exit implements Command {

    private DatabaseManager manager;

    public Exit(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("exit");
    }

    @Override
    public void process(String command) {
        manager.exit();
    }
}