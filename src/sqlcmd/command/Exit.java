package sqlcmd.command;

<<<<<<< HEAD
import sqlcmd.Console;
=======
>>>>>>> 64015ea19dc9e555812765a337a42108c7467347
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