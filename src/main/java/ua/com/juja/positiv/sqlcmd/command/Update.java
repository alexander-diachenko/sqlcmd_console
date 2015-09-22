package ua.com.juja.positiv.sqlcmd.command;

import ua.com.juja.positiv.sqlcmd.databasemanagertest.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

import java.sql.SQLException;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Update implements Command {

    private DatabaseManager manager;
    private View view;

    public Update(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (!isCorrect(command, data)) {
            return;
        }

        String[] result = new String[5];
        result[0] = data[1];
        result[1] = data[2];
        result[2] = data[3];
        boolean success = true;
        for (int index = 4; index < data.length; index += 2) {
            result[3] = data[index];
            result[4] = data[index + 1];
            try {
                manager.update(result);
            } catch (SQLException e) {
                view.write(String.format("Не удалось обновить по причине %s", e.getMessage()));
                success = false;
            }
        }
        if (success) {
            view.write("Все данные успешно обновлены.");
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length < 6 || data.length % 2 == 1) {
            view.write(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'update|tableName|primaryKeyColumnName|primaryKeyValue|" +
                    "column1Name|column1NewValue|...|" +
                    "columnNName|columnNNewValue'.", command));
            return false;
        }
        return true;
    }
}