package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Delete implements Command {

    private DatabaseManager manager;

    public Delete(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete");
    }

    @Override
    public void process(String command) {
        manager.delete(command);
    }
}
