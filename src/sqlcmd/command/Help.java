package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;

public class Help implements Command {

    private DatabaseManager manager;

    public Help(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("help");
    }

    @Override
    public void process(String command) {
        manager.help();
    }
}