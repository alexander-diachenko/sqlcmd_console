package sqlcmd.command;

import sqlcmd.databasemanager.DatabaseManager;
import sqlcmd.view.View;

import java.sql.SQLException;

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

