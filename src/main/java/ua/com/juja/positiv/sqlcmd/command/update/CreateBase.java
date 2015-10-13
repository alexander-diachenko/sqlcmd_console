package ua.com.juja.positiv.sqlcmd.command.update;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

import java.sql.SQLException;

/**
 * Created by POSITIV on 13.10.2015.
 */
public class CreateBase implements Command {

    private DatabaseManager manager;
    private View view;

    public CreateBase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("createbase|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (!isCorrect(command, data)) {
            return;
        }

        String databaseName = data[1];
        try {
            manager.createBase(databaseName);
            view.write(String.format("База '%s' успешно создана.", databaseName));
        } catch (SQLException e) {
            view.write(String.format("Не удалось создать базу '%s' " +
                    "по причине: %s", databaseName, e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length != 2) {
            view.write(String.format("Неправильная команда '%s'. " +
                    "Должно быть 'createBase|databaseName'.", command));
            return false;
        }
        return true;
    }
}
