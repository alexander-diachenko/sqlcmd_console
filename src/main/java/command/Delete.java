package command;

import databasemanagertest.DatabaseManager;
import view.View;

import java.sql.SQLException;

public class Delete implements Command {

    private DatabaseManager manager;
    private View view;

    public Delete(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (!isCorrect(command, data)) {
            return;
        }

        String tableName = data[1];
        String key = data[2];
        String value = data[2];
        try {
            manager.delete(tableName, key, value);
            view.write("Успешно удалено.");
        } catch (SQLException e) {
            view.write(String.format("Не удалось удалить поле по причине: %s", e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length != 4) {
            view.write(String.format("Неправильная команда '%s'. " +
                    "Должно быть 'delete|tableName|primaryKeyColumnName|primaryKeyValue'.", command));
            return false;
        }
        return true;
    }
}