package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Find implements Command {

    private DatabaseManager manager;

    public Find(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {

        System.out.println(manager.getTableData(command));
    }
}