package command;

import databasemanagertest.DatabaseManager;
import view.View;

import java.sql.SQLException;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class List implements Command {

    private View view;
    private DatabaseManager manager;

    public List(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("list");
    }

    @Override
    public void process(String command) {
        try {
            view.write(manager.getTableNames().toString());
        } catch (SQLException e) {
            view.write(String.format("Не удалось отобразить список таблиц по причине %s", e.getMessage()));
        }
    }
}

