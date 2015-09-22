package ua.com.juja.positiv.sqlcmd.command;

import ua.com.juja.positiv.sqlcmd.databasemanagertest.DatabaseManager;
import ua.com.juja.positiv.sqlcmd.view.View;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by POSITIV on 16.09.2015.
 */
public class Create implements Command {

    private DatabaseManager manager;
    private View view;

    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");

        if (!isCorrect(command, data)) {
            return;
        }

        String tableName = data[1];
        Map<String, Object> map = new HashMap<>();
        for (int index = 2; index < data.length; index += 2) {
           map.put(data[index], data[index + 1]);
        }

        try {
            manager.create(tableName, map);
            view.write("Запись успешно создана.");
        } catch (SQLException e) {
            view.write(String.format("Не удалось создать поле по причине: %s", e.getMessage()));
        }
    }

    private boolean isCorrect(String command, String[] data) {
        if (data.length < 3 || data.length % 2 == 1) {
            view.write(String.format("Неправильные данные '%s'. " +
                    "Должно быть 'create|tableName|column1VName|column1Value|...|" +
                    "columnNName|columnNValue'.", command));
            return false;
        }
        return true;
    }
}