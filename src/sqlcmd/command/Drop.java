package sqlcmd.command;

import sqlcmd.view.View;
import sqlcmd.databasemanager.DatabaseManager;

import java.sql.SQLException;

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

        if (data.length != 2) {
            view.write(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'drop|tableName'.", command));
            return;
        }

        String tableName = data[1];
        view.write(String.format("ВНИМАНИЕ! Вы собираетесь удалить таблицу '%s'. " +
                "Введите название таблицы для подтверждения.", tableName));
        String check = view.read();
        if (!check.equals(tableName)) {
            return;
        }

        try {
            manager.drop(tableName);
            view.write(String.format("Таблица '%s' успешно удалена.", tableName));
        } catch (SQLException e) {
            view.write(String.format("Не удалочь удалить таблицу '%s' " +
                    "по причине: %s", tableName, e.getMessage()));
        }
    }
}