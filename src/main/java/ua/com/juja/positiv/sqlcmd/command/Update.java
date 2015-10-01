package ua.com.juja.positiv.sqlcmd.command;

import ua.com.juja.positiv.sqlcmd.databasemanager.DatabaseManager;
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

        String[] columnData = new String[data.length - 4];
        String tableName = data[1];
        String primaryKey = data[2];
        String primaryKeyValue = data[3];
        for (int index = 4; index < data.length; index++) {
            columnData[index - 4] = data[index];
        }
        try {
            manager.update(tableName, primaryKey, primaryKeyValue, columnData);
            view.write("Все данные успешно обновлены.");
        } catch (SQLException e) {
            view.write(String.format("Не удалось обновить по причине %s", e.getMessage()));
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