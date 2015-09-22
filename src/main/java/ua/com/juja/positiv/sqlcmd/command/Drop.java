package ua.com.juja.positiv.sqlcmd.command;

import ua.com.juja.positiv.sqlcmd.view.View;
import ua.com.juja.positiv.sqlcmd.databasemanagertest.DatabaseManager;

import java.sql.SQLException;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Drop implements Command {

    private DatabaseManager manager;
    private View view;

    public Drop(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("drop|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (!isCorrect(command, data)) return;

        String tableName = data[1];
        if (!confirm(tableName)) return;

        try {
            manager.drop(tableName);
            view.write(String.format("Таблица '%s' успешно удалена.", tableName));
        } catch (SQLException e) {
            view.write(String.format("Не удалочь удалить таблицу '%s' " +
                    "по причине: %s", tableName, e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length != 2) {
            view.write(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'drop|tableName'.", command));
            return false;
        }
        return true;
    }

    private boolean confirm(String tableName) {
        view.write(String.format("ВНИМАНИЕ! Вы собираетесь удалить таблицу '%s'. " +
                "Введите название таблицы для подтверждения.", tableName));
        String check = view.read();
        return check.equals(tableName);
    }
}