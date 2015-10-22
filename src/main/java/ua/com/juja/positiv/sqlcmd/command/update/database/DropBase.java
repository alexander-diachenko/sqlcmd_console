package ua.com.juja.positiv.sqlcmd.command.update.database;

import ua.com.juja.positiv.sqlcmd.command.Command;
import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

import java.sql.SQLException;

/**
 * Created by POSITIV on 13.10.2015.
 */
public class DropBase implements Command {

    private DatabaseManager manager;
    private View view;

    public DropBase(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("dropbase|");
    }

    @Override
    public String format() {
        return "dropbase|databaseName";
    }

    @Override
    public String description() {
        return "удаление базы";
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (!isCorrect(command, data)) {
            return;
        }

        String databaseName = data[1];
        if (!confirmed(databaseName)) {
            return;
        }

        try {
            manager.dropBase(databaseName);
            view.write(String.format("База '%s' успешно удалена.", databaseName));
        } catch (SQLException e) {
            view.write(String.format("Не удалось удалить базу '%s' " +
                    "по причине: %s", databaseName, e.getMessage()));
        }

    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length != 2) {
            view.write(String.format("Неправильная команда '%s'. " +
                    "Должно быть 'dropBase|databaseName'.", command));
            return false;
        }
        return true;
    }

    private boolean confirmed(String tableName) {
        view.write(String.format("ВНИМАНИЕ! Вы собираетесь удалить базу '%s'. " +
                "Введите название базы для подтверждения.", tableName));
        String check = view.read();
        if(check.equals(tableName)){
            return true;
        }
        view.write("Удаление отменено.");
        return false;
    }
}
