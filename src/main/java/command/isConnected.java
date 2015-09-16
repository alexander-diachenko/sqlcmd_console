package command;

import databasemanagertest.DatabaseManager;
import view.View;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class isConnected implements Command {
    private DatabaseManager manager;
    private View view;

    public isConnected(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return !manager.isConnected();
    }

    @Override
    public void process(String command) {
        view.write(String.format("Вы не можете пользоваться командой '%s' без подключения к базе.", command));
    }
}
